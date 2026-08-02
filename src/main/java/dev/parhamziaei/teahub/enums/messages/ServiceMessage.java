package dev.parhamziaei.teahub.enums.messages;

public enum ServiceMessage {

    // ? defaults
    DEFAULT_EDITED("success.service.default.edited"),
    DEFAULT_ACTION_DONE("success.service.default.action-done"),
    DEFAULT_ACTION_FAILED("success.service.default.action-failed"),
    DEFAULT_CREATED("success.service.default.created"),
    DEFAULT_CONFLICTION("error.service.default.confliction"),
    DEFAULT_IN_USE("error.service.default.in-use"),
    DEFAULT_DELETED("success.service.default.deleted"),
    DEFAULT_NO_SUCH_DATA("error.service.default.no-such-data"),
    DEFAULT_NO_SUCH_ENTITY("error.service.default.no-such-entity"),
    DEFAULT_EXTERNAL_SERVICE_UNAVAILABLE("error.service.external-default-unavailable"),

    // ? ticket service messages
    TICKET_MAX_ATTACHMENT_REACHED("error.service.ticket.max_attachment_reached"),
    TICKET_MESSAGE_SENT("success.service.ticket.message_sent"),
    TICKET_SUBMITTED("success.service.ticket.submitted"),
    TICKET_EDITED("success.service.ticket.edited"),

    // ? file storage service errors
    PAYLOAD_TOO_LARGE("error.service.file.payload-too-large"),
    MEDIA_TYPE_NOT_ALLOWED("error.service.file.type-not-allowed"),
    FILE_STORAGE_ERROR("error.service.file.storage"),

    // ? user messages
    USER_WALLET_CHARGE_AMOUNT_TOO_SMALL("error.service.wallet.charge-amount-too-small"),
    USER_WALLET_INSUFFICIENT_BALANCE("error.service.wallet.insufficient-balance"),

    // ? shop
    SHOP_CATEGORY_NOT_FOUND("error.service.shop.category-not-found"),

    // ? payment messages
    PAYMENT_ERROR("error.service.payment"),
    PAYMENT_FAILED("error.service.payment-failed"),
    PAYMENT_INVOICE_ERROR("error.service.invoice"),
    PAYMENT_INVOICE_CREATED("success.service.invoice.created"),
    PAYMENT_INVOICE_PAID("success.service.invoice.paid"),

    PAYMENT_GATEWAY_CREATED("success.service.gateway.created"),
    PAYMENT_GATEWAY_ERROR("error.service.gateway"),
    PAYMENT_GATEWAY_CONFIG_ERROR("error.service.gateway.config"),
    PAYMENT_GATEWAY_NOT_FOUND("error.service.gateway.not-found"),
    PAYMENT_INSUFFICIENT_BALANCE("error.service.insufficient-balance"),

    // ? resource management messages
    RESOURCE_PROCESSING("success.service.resource.deploying"),
    RESOURCE_EDITED("success.service.resource.edited"),
    RESOURCE_PROLONGED("success.service.resource.prolonged"),
    RESOURCE_SUSPENDED("success.service.resource.suspended"),
    RESOURCE_PROVISION_ERROR("success.service.resource.provision-error"),

    // ? query instance management messages
    QUERY_INSTANCE_INITIATED("success.service.query-instance.initiated"),
    QUERY_INSTANCE_ALREADY_EXIST("error.service.query-instance.already-exists"),
    QUERY_INSTANCE_ENABLING("success.service.query-instance.enabling"),
    QUERY_INSTANCE_DISABLING("success.service.query-instance.disabling"),
    QUERY_INSTANCE_DELETED("success.service.query-instance.deleted"),
    QUERY_INSTANCE_PORT_RANGE_INVALID("error.service.query-instance.port-range-invalid"),
    QUERY_INSTANCE_NOT_FOUND("error.service.query-instance.not-found"),
    QUERY_INSTANCE_ERROR("error.service.query-instance"),
    QUERY_INSTANCE_SYNCING_FAILED("error.service.query-instance.syncing-failed"),

    // ? audio bot node management messages
    AUDIO_BOT_NODE_INITIATED("success.service.audio-bot-node.initiated"),
    AUDIO_BOT_NODE_EDITED("success.service.audio-bot-node.edited"),
    AUDIO_BOT_NODE_WEB_ADDRESS_ALREADY_INITIATED("success.audio-bot-node.already-initiated"),
    AUDIO_BOT_NODE_HAS_ACTIVE_INSTANCE("success.audio-bot-node.has-active-instance"),
    AUDIO_BOT_MUST_BE_CONNECTED("error.audio-bot-instance.must-be-connected"),

    PROVISIONING_STRATEGY_CHANGED("success.service.provisioning-strategy-changed"),

    DNS_PROVIDER_NOT_CONFIGURED("error.service.dns-provider.not-configured"),
    DNS_PROVIDER_API_ERROR("error.service.dns-provider.api-error"),
    RESOURCE_ALREADY_HAS_SUBDOMAIN("error.service.dns-provider.resource-already-has-subdomain");

    private final String key;

    ServiceMessage(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
