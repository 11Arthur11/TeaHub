package dev.parhamziaei.teahub.integration;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.audio_bot.component.AudioBotGateway;
import dev.parhamziaei.teahub.integration.audio_bot.exception.AudioBotProvisionException;
import dev.parhamziaei.teahub.integration.audio_bot.internal_service.BalancedAudioBotProvisionStrategy;
import dev.parhamziaei.teahub.integration.audio_bot.internal_service.RandomizedAudioBotProvisionStrategy;
import dev.parhamziaei.teahub.integration.audio_bot.internal_service.RoundRobinAudioBotProvisionStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryProvisionException;
import dev.parhamziaei.teahub.integration.teaspeak_query.internal_service.BalancedTeaSpeakProvisionStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.internal_service.RoundRobinTeaSpeakProvisionStrategy;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProvisionStrategyTest {

    @Test
    void balancedTeaSpeakSelectsLeastLoadedInstance() {
        QueryInstanceRepository repository = mock(QueryInstanceRepository.class);
        QueryInstance busy = queryInstance(1L, 3);
        QueryInstance free = queryInstance(2L, 1);
        when(repository.findProvisionCandidates()).thenReturn(List.of(busy, free));

        QueryInstance result = new BalancedTeaSpeakProvisionStrategy(repository).getProviderQueryInstance();

        assertSame(free, result);
    }

    @Test
    void roundRobinTeaSpeakIsStableAndCyclesInIdOrder() {
        QueryInstanceRepository repository = mock(QueryInstanceRepository.class);
        QueryInstance first = queryInstance(1L, 0);
        QueryInstance second = queryInstance(2L, 0);
        when(repository.findProvisionCandidates()).thenReturn(List.of(second, first));
        RoundRobinTeaSpeakProvisionStrategy strategy = new RoundRobinTeaSpeakProvisionStrategy(repository);

        assertSame(first, strategy.getProviderQueryInstance());
        assertSame(second, strategy.getProviderQueryInstance());
        assertSame(first, strategy.getProviderQueryInstance());
    }

    @Test
    void teaSpeakStrategyFailsClearlyWithoutCandidate() {
        QueryInstanceRepository repository = mock(QueryInstanceRepository.class);
        when(repository.findProvisionCandidates()).thenReturn(List.of());

        assertThrows(
                QueryProvisionException.class,
                () -> new BalancedTeaSpeakProvisionStrategy(repository).getProviderQueryInstance()
        );
    }

    @Test
    void balancedAudioBotChecksHealthInLoadOrder() {
        AudioBotNodeRepository repository = mock(AudioBotNodeRepository.class);
        AudioBotGateway gateway = mock(AudioBotGateway.class);
        AudioBotNode busy = audioNode(1L, 3);
        AudioBotNode freeButDown = audioNode(2L, 0);
        AudioBotNode healthy = audioNode(3L, 1);
        when(repository.findProvisionCandidates()).thenReturn(List.of(busy, healthy, freeButDown));
        when(gateway.testConnection(freeButDown)).thenReturn(false);
        when(gateway.testConnection(healthy)).thenReturn(true);

        AudioBotNode result = new BalancedAudioBotProvisionStrategy(repository, gateway).getProviderNode();

        assertSame(healthy, result);
        verify(gateway).testConnection(freeButDown);
        verify(gateway).testConnection(healthy);
        verify(gateway, never()).testConnection(busy);
    }

    @Test
    void roundRobinAudioBotSkipsUnhealthyNodeAndCycles() {
        AudioBotNodeRepository repository = mock(AudioBotNodeRepository.class);
        AudioBotGateway gateway = mock(AudioBotGateway.class);
        AudioBotNode down = audioNode(1L, 0);
        AudioBotNode healthy = audioNode(2L, 0);
        when(repository.findProvisionCandidates()).thenReturn(List.of(healthy, down));
        when(gateway.testConnection(down)).thenReturn(false);
        when(gateway.testConnection(healthy)).thenReturn(true);
        RoundRobinAudioBotProvisionStrategy strategy = new RoundRobinAudioBotProvisionStrategy(repository, gateway);

        assertSame(healthy, strategy.getProviderNode());
        assertSame(healthy, strategy.getProviderNode());
    }

    @Test
    void audioBotStrategyFailsWhenEveryNodeIsUnhealthy() {
        AudioBotNodeRepository repository = mock(AudioBotNodeRepository.class);
        AudioBotGateway gateway = mock(AudioBotGateway.class);
        AudioBotNode down = audioNode(1L, 0);
        when(repository.findProvisionCandidates()).thenReturn(List.of(down));

        assertThrows(
                AudioBotProvisionException.class,
                () -> new BalancedAudioBotProvisionStrategy(repository, gateway).getProviderNode()
        );
    }

    @Test
    void randomizedAudioBotTriesEveryCandidateBeforeFailing() {
        AudioBotNodeRepository repository = mock(AudioBotNodeRepository.class);
        AudioBotGateway gateway = mock(AudioBotGateway.class);
        AudioBotNode first = audioNode(1L, 0);
        AudioBotNode second = audioNode(2L, 0);
        AudioBotNode third = audioNode(3L, 0);
        when(repository.findProvisionCandidates()).thenReturn(List.of(first, second, third));

        assertThrows(
                AudioBotProvisionException.class,
                () -> new RandomizedAudioBotProvisionStrategy(repository, gateway).getProviderNode()
        );

        verify(gateway).testConnection(first);
        verify(gateway).testConnection(second);
        verify(gateway).testConnection(third);
    }

    private QueryInstance queryInstance(long id, int load) {
        QueryInstance instance = new QueryInstance();
        instance.setId(id);
        instance.setActive(true);
        instance.setInstances(new ArrayList<>());
        for (int i = 0; i < load; i++) {
            instance.getInstances().add(null);
        }
        return instance;
    }

    private AudioBotNode audioNode(long id, int load) {
        AudioBotNode node = new AudioBotNode();
        node.setId(id);
        node.setEnabled(true);
        node.setInstances(new ArrayList<>());
        for (int i = 0; i < load; i++) {
            node.getInstances().add(null);
        }
        return node;
    }
}
