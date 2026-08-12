# TeaHub Backend

**TeaHub** is the backend and infrastructure orchestration layer of the **TeaCloud** platform.

TeaCloud is composed of two main applications:

- **TeaHub** — Backend
- **TeaCup** — Frontend

Together, they form the complete **TeaCloud** platform.

TeaHub is responsible for authentication, users, billing, products, service lifecycle management, infrastructure provisioning, TeaSpeak instances, AudioBot nodes, DNS provisioning, payments, notifications, support tickets, system configuration, and live operational events.

**Current backend artifact version: `0.0.2`**

---

# فارسی

## معرفی

TeaHub هسته Backend پلتفرم TeaCloud است.

این پروژه تنها یک REST API ساده نیست؛ بلکه وظیفه مدیریت Lifecycle سرویس‌های TeaCloud و ارتباط میان بخش‌های مختلف زیرساخت را نیز برعهده دارد.

TeaHub بین کاربران، سیستم مالی، محصولات، Resourceها و Providerهای خارجی قرار می‌گیرد و عملیات‌هایی مانند خرید سرویس، Provisioning، تمدید، تعلیق، مدیریت Nodeها، پرداخت و DNS را هماهنگ می‌کند.

فرانت‌اند TeaCloud با نام **TeaCup** از APIهای TeaHub استفاده می‌کند.

معماری کلی:

```text
TeaCup
  │
  │ HTTPS / REST / WebSocket
  ▼
TeaHub
  │
  ├── Authentication
  ├── Users
  ├── Products
  ├── Billing
  ├── Resources
  ├── Provisioning
  ├── TeaSpeak
  ├── AudioBot
  ├── DNS
  ├── Tickets
  └── System Services
```

---

# تکنولوژی‌ها

TeaHub با **Java 21** و **Spring Boot** توسعه داده شده است.

تکنولوژی‌های اصلی پروژه:

- Java 21
- Spring Boot 3
- Spring Web
- Spring Security
- Spring Data JPA
- Spring JDBC
- MySQL
- Redis
- Apache Kafka
- Spring WebSocket
- STOMP
- JWT
- Bean Validation
- MapStruct
- ModelMapper
- Caffeine Cache
- Bucket4j
- Spring Actuator
- Spring Mail
- SpringDoc / OpenAPI
- Lombok
- Maven

Integrationهای اختصاصی نیز برای سرویس‌های زیر وجود دارند:

- TeaSpeak / TeamSpeak ServerQuery
- AudioBot infrastructure
- Liara DNS
- IPPanel SMS
- Aqaye Pardakht payment gateway

---

# معماری

ساختار TeaHub عمدتاً از معماری Layered همراه با بخش‌های Integration و Event-driven تشکیل شده است.

جریان معمول یک Request:

```text
Controller
    ↓
Service
    ↓
Repository / Integration / Event
    ↓
Database / Redis / Kafka / External Provider
```

پکیج‌های اصلی پروژه شامل موارد زیر هستند:

```text
controller/
service/
repository/
entity/
dto/
integration/
kafka/
configuration/
component/
validation/
exception/
schedule/
valueobject/
```

Controllerها نیز به سه گروه اصلی تقسیم شده‌اند:

```text
controller/global
controller/user
controller/admin
```

این جداسازی باعث می‌شود APIهای Public، User و Admin از نظر مسئولیت و سطح دسترسی مستقل باقی بمانند.

---

# Domain Model

TeaHub چند Domain اصلی را مدیریت می‌کند.

## Users

شامل:

- User
- Role
- User settings
- Refresh token
- Wallet
- Online state
- Authentication sessions

Roleهای اصلی:

```text
ROLE_USER
ROLE_SUPPORT
ROLE_ADMIN
```

---

## Shop

سیستم Shop مسئول تعریف محصولاتی است که در TeaCloud فروخته می‌شوند.

دو خانواده اصلی Product وجود دارند:

```text
TEASPEAK
AUDIO_BOT
```

هر Product می‌تواند مشخصاتی مانند موارد زیر داشته باشد:

- Name
- Category
- Price
- Period
- Presentation
- Enabled state
- Service-specific configuration

Categoryها نیز مستقل مدیریت می‌شوند و برای ساخت Catalog فرانت‌اند استفاده می‌شوند.

---

# Resource Model

پس از خرید یک Product، Resource واقعی کاربر ساخته می‌شود.

دو Resource اصلی سیستم:

```text
TeaSpeakResource
AudioBotResource
```

هر Resource دارای Lifecycle مستقل است.

وضعیت‌های اصلی شامل:

```text
DEPLOYING
ACTIVE
PENDING_PROLONG
```

عملیات عمومی Resource:

- Create
- Edit
- Prolong
- Auto prolong
- Expiration
- Suspension
- Deletion
- Provisioning

منطق مشترک Resourceها در `BillableResource` قرار می‌گیرد و رفتار تخصصی هر سرویس در Resource مربوط به خودش پیاده‌سازی می‌شود.

---

# Resource Provisioning

یکی از مسئولیت‌های مهم TeaHub، Provision کردن سرویس روی زیرساخت واقعی است.

به‌جای قراردادن منطق همه سرویس‌ها در یک Service بزرگ، پروژه از Strategy و Factory استفاده می‌کند.

ساختار کلی:

```text
DeploymentStrategyFactory
        │
        ├── TeaSpeakDeploymentHandler
        │
        └── AudioBotDeploymentHandler
```

بنابراین Service اصلی Resource لازم نیست جزئیات Provision شدن هر Provider را بداند.

این طراحی امکان اضافه‌شدن Resource Typeهای جدید را ساده‌تر می‌کند.

---

# Provisioning Strategy

TeaHub می‌تواند Resourceها را میان چند Node توزیع کند.

Strategyهای فعلی:

```text
BALANCED
BIN_PACKING
RANDOMIZED
ROUND_ROBIN
```

این Strategyها هم برای TeaSpeak Query Instanceها و هم برای AudioBot Nodeها استفاده می‌شوند.

هر زیرساخت Factory و Handler مستقل خود را دارد.

---

# TeaSpeak Infrastructure

مدیریت TeaSpeak از طریق Query Instanceها انجام می‌شود.

هر Query Instance شامل اطلاعاتی مانند:

- Query address
- Query port
- Username
- Password
- Maximum instance capacity
- Port range
- Status
- Enabled state

است.

TeaHub برای ارتباط با TeaSpeak از یک لایه Integration اختصاصی استفاده می‌کند.

این لایه شامل:

- Telnet connection pool
- Query CLI
- Command factory
- Response decoder
- Session management

است.

عملیات‌هایی مانند:

- Server creation
- Start
- Stop
- Server information
- Privilege management

از طریق همین Integration انجام می‌شوند.

---

# AudioBot Infrastructure

AudioBotها روی Nodeهای مستقل اجرا می‌شوند.

هر Node دارای اطلاعاتی مانند:

- Web address
- Username
- Password
- Capacity
- Enabled state
- Node status
- Active instance count

است.

TeaHub مسئول:

- Node registration
- Node editing
- Capacity management
- Provisioning
- Start / Stop
- Synchronization
- Scoped panel access

است.

AudioBot وضعیت‌هایی مانند:

```text
OFFLINE
CONNECTING
CONNECTED
```

دارد.

---

## Scoped AudioBot Panel

مدیریت کامل Media و Playlist به پنل اختصاصی AudioBot منتقل شده است.

TeaHub برای هر Resource می‌تواند Access محدودشده ایجاد کند:

```text
GET /v1/services/audio-bot/{resourceId}/access
```

Response شامل:

```text
panelAddress
credentials
validUntil
```

است.

به این ترتیب کاربر برای مدیریت AudioBot به پنل اختصاصی همان Resource هدایت می‌شود، بدون اینکه Credentialهای اصلی Node در اختیار او قرار گیرند.

---

# Event-Driven Processing

بخشی از عملیات زیرساخت TeaHub به‌صورت Event-driven اجرا می‌شود.

Apache Kafka برای انتقال Eventهای مربوط به Resource و Infrastructure استفاده می‌شود.

ساختار Kafka شامل:

```text
producer/
consumer/
event/
handler/
configuration/
```

است.

نمونه Eventها:

```text
TeaSpeakDeployEvent
AudioBotDeployEvent
ResourceDeleteEvent
ResourceExpiredEvent
ResourceDeployFailedEvent
TelnetSessionUnreachableEvent
```

این معماری باعث می‌شود عملیات Provisioning و Lifecycle سرویس‌ها از Request اصلی HTTP جدا شوند.

---

# Authentication

Authentication در TeaHub بر پایه:

- Phone number
- OTP
- Redis-backed temporary sessions
- JWT
- Cookies

طراحی شده است.

جریان کلی:

```text
POST /v1/auth/initiate
        ↓
Login or Registration decision
        ↓
OTP Verification
        ↓
Login / Register
        ↓
JWT Cookie
        ↓
Authenticated Session
```

---

## Authentication Initiation

کاربر ابتدا شماره موبایل را ارسال می‌کند.

TeaHub مشخص می‌کند که Flow باید:

```text
LOGIN_INITIATED
```

یا:

```text
REGISTER_INITIATED
```

باشد.

OTP و Sessionهای موقت Authentication در Redis نگهداری می‌شوند.

---

## JWT Authentication

بعد از Login یا Registration موفق، Authentication اصلی از طریق JWT انجام می‌شود.

پروژه دارای:

```text
JwtAuthFilter
AuthenticationFactory
CookieFactory
JwtService
RefreshToken
```

است.

JWT از طریق Cookie به Client تحویل داده می‌شود و TeaCup نیازی به ذخیره Token در `localStorage` ندارد.

---

## Session Check

Frontend می‌تواند وضعیت Authentication را از طریق:

```text
HEAD /v1/auth/session
```

بررسی کند.

این Endpoint برای تشخیص سریع وضعیت Session بدون دریافت Profile کامل استفاده می‌شود.

---

# Security

TeaHub از Spring Security برای کنترل Authentication و Authorization استفاده می‌کند.

Security Layer شامل مواردی مانند:

- JWT validation
- Role-based authorization
- Authentication filters
- Cookie management
- Password validation
- Request validation
- Rate limiting

است.

Validationهای اختصاصی پروژه نیز شامل:

```text
@PhoneNumber
@PasswordValidation
@SafeName
@Slug
@Subdomain
@IpAddress
@EnumValue
```

هستند.

---

# Rate Limiting

TeaHub از Bucket4j برای Rate Limiting استفاده می‌کند.

این موضوع به‌خصوص برای Endpointهای حساس مانند Authentication اهمیت دارد.

در صورت عبور از محدودیت، API می‌تواند:

```text
429 Too Many Requests
```

برگرداند.

TeaCup نیز 429 را به‌صورت Global Rate Limit تفسیر می‌کند و درخواست‌های خودکار را تا Refresh دستی متوقف می‌کند.

---

# Redis

Redis در TeaHub برای Stateهایی استفاده می‌شود که لزوماً نباید در دیتابیس اصلی نگهداری شوند.

نمونه‌ها:

```text
PhoneVerifySession
TwoFactorSession
OnlineUser
```

Redis همچنین برای Authentication و وضعیت‌های کوتاه‌عمر مناسب استفاده می‌شود.

---

# Persistence

Persistence اصلی TeaHub بر پایه:

```text
Spring Data JPA
MySQL
```

است.

Repositoryهای اختصاصی برای Domainهای مختلف وجود دارند:

- Users
- Resources
- Products
- Wallet
- Invoice
- Ticket
- DNS
- Query Instances
- AudioBot Nodes
- Notifications

برای Queryهای پیچیده از:

- Custom repository implementations
- JPA Specifications
- Aggregates

استفاده می‌شود.

---

# Mapping

TeaHub DTO و Entity را مستقیماً به یکدیگر وابسته نمی‌کند.

برای Mapping از MapStruct استفاده شده است.

نمونه Mapperها:

```text
UserMapStruct
AudioBotMapStruct
BillableResourceMapStruct
QueryInstanceMapStruct
InvoicePropertiesMapStruct
AppSettingsMapStruct
```

در بعضی قسمت‌ها نیز ModelMapper وجود دارد.

Patch-like Editها می‌توانند با Mapping مقادیر غیر null انجام شوند تا فقط فیلدهایی که واقعاً تغییر کرده‌اند روی Entity اعمال شوند.

---

# Billing System

TeaHub دارای Wallet و Invoice System داخلی است.

اجزای اصلی:

```text
Wallet
WalletTransaction
Invoice
PaymentTransaction
Gateway
```

Wallet Transactionها شامل:

```text
CREDIT
DEBIT
```

و Reasonهایی مانند:

```text
PURCHASE
PROLONG
REFUND
WALLET_CHARGE
```

هستند.

---

# Invoice System

Invoiceها می‌توانند برای موارد مختلف ایجاد شوند، مانند:

- Purchase
- Resource prolong
- Wallet charge
- Admin debt

هر Invoice دارای:

- Amount
- Status
- Tax percentage
- Payment transaction
- Post-payment action

است.

وضعیت‌های اصلی:

```text
PENDING
PAID
CANCELLED
```

---

## Tax

مقدار مالیات به‌صورت درصد صحیح ذخیره و منتقل می‌شود.

مثلاً:

```text
9 = 9%
```

Tax calculation:

```text
tax = amount × taxPercentage / 100
```

---

# Post-Payment Architecture

بعد از پرداخت موفق، رفتار سیستم می‌تواند بر اساس نوع Invoice متفاوت باشد.

برای این منظور TeaHub از Registry Pattern استفاده می‌کند.

نمونه Handlerها:

```text
WalletChargePostPaymentRegistry
ProlongPostPaymentRegistry
AdminDebtPostPaymentRegistry
```

بنابراین Payment Gateway مسئول منطق Business پس از پرداخت نیست.

---

# Payment Gateways

Payment Gatewayها پشت یک لایه abstraction قرار دارند.

ساختار:

```text
PaymentGatewayFactory
PaymentGatewayHandler
```

Integration فعلی شامل Aqaye Pardakht است.

این معماری امکان اضافه‌کردن Providerهای پرداخت جدید را بدون تغییر گسترده Business Logic فراهم می‌کند.

---

# DNS Provisioning

TeaHub سیستم DNS Provisioning داخلی دارد.

اجزای اصلی:

```text
BaseDnsProvider
DnsZone
DnsRecord
ADnsRecord
SrvDnsRecord
```

DNS Providerها توسط Registry مدیریت می‌شوند:

```text
DnsProviderRegistry
DnsProviderGateway
```

Provider فعلی:

```text
Liara
```

است.

---

## User DNS

کاربر می‌تواند Subdomain را به TeaSpeak Resource اختصاص دهد.

Flow کلی:

```text
Check zone
    ↓
Check subdomain availability
    ↓
Create DNS record
    ↓
Assign to Resource
```

TeaHub Validation مستقل برای Subdomain دارد.

---

## Admin DNS

ادمین می‌تواند:

- Provider configuration را مدیریت کند
- Zoneها را مشاهده کند
- Zone را فعال یا غیرفعال کند
- Records را مشاهده کند
- Record را Unassign کند
- Record را ReAssign کند

---

# Tickets

TeaHub دارای Support Ticket System داخلی است.

Ticket شامل:

- Subject
- Department
- Status
- Messages
- Attachments
- Related resource

است.

Departmentها:

```text
TECHNICAL
SALES
```

Statusها:

```text
PENDING
CLOSED
RESPONDED
WAITING
```

User، Support و Admin هرکدام APIهای مناسب Role خود را دارند.

---

# File Attachments

Ticketها از Attachment پشتیبانی می‌کنند.

File storage دارای Validation و Exceptionهای مستقل است، از جمله:

```text
MediaNotFoundException
MediaSizeTooLargeException
MediaTypeNotAllowedException
FileStorageServiceException
```

بنابراین کنترل فایل از Business Logic تیکت جدا شده است.

---

# Notifications

TeaHub از Notificationهای سیستمی پشتیبانی می‌کند.

Notificationها می‌توانند توسط Admin ایجاد و مدیریت شوند و برای Userها نمایش داده شوند.

همچنین Infrastructure ارسال پیام شامل:

- SMS
- Email

است.

Integration پیامک فعلی از IPPanel استفاده می‌کند.

---

# Application Settings

برخی رفتارهای Business بدون Deploy مجدد Backend قابل تغییر هستند.

Application Settings شامل بخش‌هایی مانند:

```text
InvoiceProperties
ProductPeriodSettings
```

است.

نمونه تنظیمات:

- Minimum wallet charge
- Tax percentage
- Resource cleanup timing

این مقادیر از Admin API قابل خواندن و ویرایش هستند.

---

# Scheduled Tasks

TeaHub دارای Taskهای زمان‌بندی‌شده برای Maintenance است.

نمونه‌ها:

```text
ResourceExpirationSchedule
NotificationExpirationSchedule
```

این Taskها مسئول رسیدگی به Lifecycleهایی هستند که مستقل از Request کاربر اتفاق می‌افتند.

---

# Live Logs

TeaHub قابلیت ارسال Live Log به پنل Admin را دارد.

زیرساخت شامل:

```text
WebSocketConfig
WebSocketLogAppender
LogWebSocketService
```

است.

Log Event شامل:

```text
logger
level
message
thread
timestamp
```

می‌شود.

TeaCup از طریق WebSocket/STOMP این Logها را دریافت و در پنل System نمایش می‌دهد.

---

# Caching

TeaHub از Spring Cache و Caffeine استفاده می‌کند.

Caching برای کاهش Queryهای تکراری و افزایش Performance بخش‌هایی که Data آنها در هر Request تغییر نمی‌کند قابل استفاده است.

---

# Error Handling

Exception handling در سطح مرکزی پروژه انجام می‌شود.

دو Handler اصلی:

```text
AuthenticationExceptionHandler
GlobalExceptionHandler
```

وجود دارند.

همچنین Exceptionهای Domain-specific برای حوزه‌های مختلف تعریف شده‌اند:

- Authentication
- Authorization
- AudioBot
- TeaSpeak
- DNS
- Payments
- Resources
- Tickets
- Storage
- Users

این ساختار باعث می‌شود Errorها به‌صورت Business-oriented مدیریت شوند.

---

# API Response Model

TeaHub از Response Wrapperهای مشترک استفاده می‌کند.

انواع اصلی:

```text
SimpleResponse
DataResponse<T>
DetailedDataResponse<T>
```

Responseها معمولاً شامل:

```text
success
type
data
message
```

هستند.

این قرارداد باعث می‌شود TeaCup بتواند Responseهای Backend را به‌شکل یکسان مدیریت کند.

---

# OpenAPI

APIهای TeaHub با SpringDoc/OpenAPI مستندسازی می‌شوند.

OpenAPI قرارداد اصلی ارتباط TeaHub و TeaCup است.

این Specification شامل:

- Paths
- Request models
- Response models
- Enums
- Filters
- Admin APIs
- User APIs

است.

TeaCup نیز Modelها و Operationهای TypeScript خود را بر اساس همین قرارداد تولید می‌کند.

---

# Configuration

Configurationهای اصلی در:

```text
application.yaml
```

قرار دارند.

همچنین Configuration جداگانه‌ای برای محیط Docker وجود دارد:

```text
application-docker.yaml
```

Configuration Properties مستقل برای قسمت‌هایی مانند موارد زیر تعریف شده‌اند:

```text
JWT
Session
Redis
IPPanel
Payment
Query Instance
Notifications
Cookies
Application Settings
```

---

# Project Structure

ساختار خلاصه‌شده پروژه:

```text
src/
├── main/
│   ├── java/dev/parhamziaei/teahub/
│   │   ├── component/
│   │   ├── configuration/
│   │   ├── controller/
│   │   │   ├── admin/
│   │   │   ├── global/
│   │   │   └── user/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── exception/
│   │   ├── integration/
│   │   ├── kafka/
│   │   ├── repository/
│   │   ├── schedule/
│   │   ├── service/
│   │   ├── validation/
│   │   └── valueobject/
│   └── resources/
│       ├── application.yaml
│       └── application-docker.yaml
└── test/
```

---

# Tests

پروژه دارای تست برای بخش‌های مهم Business است.

نمونه‌ها:

```text
JwtServiceTest
ResourceServiceTest
WalletServiceTest
TeaSpeakApiTest
```

همچنین Test Utilityهای جداگانه برای ساخت Entityهای موردنیاز تست‌ها وجود دارند.

---

# Development

## Requirements

برای اجرای پروژه به‌طور معمول نیاز به موارد زیر است:

```text
Java 21
Maven
MySQL
Redis
Kafka
```

بسته به Featureهایی که فعال هستند، Credential سرویس‌های خارجی مانند DNS، SMS یا Payment نیز موردنیاز خواهد بود.

---

## Build

```bash
mvn clean package
```

---

## Run

```bash
mvn spring-boot:run
```

یا:

```bash
java -jar target/TeaHub-0.0.2.jar
```

---

## Tests

```bash
mvn test
```

---