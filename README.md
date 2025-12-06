# 📘 API Endpoints Documentation

# Admin Controllers

## 🎫 Admin Tickets Manager
| Method | Endpoint                                                 | Description |
|--------|-----------------------------------------------------------|-------------|
| **PUT**  | /v1/admin/tickets/edit/{ticketId}                        | ویرایش تیکت توسط ادمین |
| **POST** | /v1/admin/tickets/submit                                 | ایجاد تیکت جدید توسط ادمین |
| **GET**  | /v1/admin/tickets                                        | دریافت همه تیکت‌ها |
| **GET**  | /v1/admin/tickets/{phoneNumber}                          | دریافت تیکت‌های یک کاربر با شماره |
| **GET**  | /v1/admin/tickets/detail/{ticketId}                      | مشاهده جزئیات تیکت توسط ادمین |

## 🎫 Admin Product Manager
| Method   | Endpoint                               | Description |
|----------|-----------------------------------------|-------------|
| **GET**  | /v1/admin/products                      | دریافت همه محصولات TeaSpeak |
| **POST** | /v1/admin/products                      | ایجاد محصول جدید TeaSpeak |
| **GET**  | /v1/admin/products/{productId}          | دریافت اطلاعات یک محصول خاص |
| **POST** | /v1/admin/products/edit                 | ویرایش/به‌روزرسانی محصول موجود |
| **DELETE** | /v1/admin/products/{productId}        | حذف یک محصول |

## 💳 Admin Payment
| Method | Endpoint                                                 | Description |
|--------|-----------------------------------------------------------|-------------|
| **POST** | /v1/admin/payments/gateways/init                         | ایجاد درگاه پرداخت |
| **GET**  | /v1/admin/payments/gateways/{id}                         | مشاهده جزئیات یک درگاه |
| **GET**  | /v1/admin/payments/gateways/modules                      | دریافت لیست ماژول‌های درگاه‌ها |

## 💻 Admin Query Instance Manager
| Method | Endpoint                                                 | Description |
|--------|-----------------------------------------------------------|-------------|
| **POST** | /v1/admin/query-instances/initiate                       | ایجاد Query Instance جدید |
| **PATCH** | /v1/admin/query-instances/{id}/enable                   | فعال‌سازی Instance |
| **PATCH** | /v1/admin/query-instances/{id}/disable                  | غیرفعال‌سازی Instance |
| **GET**  | /v1/admin/query-instances                                | لیست همه Instances |
| **DELETE** | /v1/admin/query-instances/{id}/remove                  | حذف Instance |

# User Controllers

## 👤 User Controller
| Method | Endpoint                                                 | Description |
|--------|-----------------------------------------------------------|-------------|
| **POST** | /v1/users/wallet/charge                                  | شارژ کیف پول و دریافت invoiceToken |

## 🎫 User Tickets
| Method | Endpoint                                                 | Description |
|--------|-----------------------------------------------------------|-------------|
| **POST** | /v1/tickets/submit                                       | ارسال تیکت جدید توسط کاربر |
| **PUT**  | /v1/tickets/detail/{ticketId}/message                    | افزودن پیام جدید به تیکت موجود |
| **GET**  | /v1/tickets                                               | دریافت لیست تیکت‌های کاربر |
| **GET**  | /v1/tickets/detail/{id}                                  | دریافت جزئیات کامل یک تیکت |
| **GET**  | /v1/tickets/attachment/{identifier}                      | دریافت فایل پیوست تیکت |

## 💵 Payment
| Method | Endpoint                                                 | Description |
|--------|-----------------------------------------------------------|-------------|
| **POST** | /v1/payments/pay                                         | ارسال درخواست پرداخت |
| **POST** | /v1/payments/gateway/callback/ap                         | کال‌بک درگاه پرداخت |
| **GET**  | /v1/payments/gateways                                    | لیست درگاه‌های پرداخت |

## 💴 Invoices
| Method | Endpoint                                                 | Description |
|--------|-----------------------------------------------------------|-------------|
| **GET**  | /v1/invoices                                              | لیست فاکتورها |
| **GET**  | /v1/invoices/{invoiceToken}                               | مشاهده جزئیات فاکتور |

# Global Controllers

## 🔐 Authentication
| Method | Endpoint                                                 | Description |
|--------|-----------------------------------------------------------|-------------|
| **POST** | /v1/auth/register                                        | ثبت‌نام کاربر |
| **POST** | /v1/auth/login                                           | ورود |
| **POST** | /v1/auth/logout                                          | خروج |
| **POST** | /v1/auth/initiate                                        | شروع فرآیند احراز هویت (مثل ارسال OTP) |

## 📂 Test
| Method | Endpoint                                                 | Description |
|--------|-----------------------------------------------------------|-------------|
| **GET**  | /v1/test/ip                                               | دریافت IP درخواست |



# 🍃 Explain concept of Spring & Spring Security
<img width="2175" height="1226" alt="Untitled-2025-10-22-2359" src="https://github.com/user-attachments/assets/c3034c6f-6ed1-43a6-b315-2a158b372314" />


# 🔒 Explain of Jwt Authentication Filter LifeCycle
<img width="1831" height="1283" alt="jwt lifecycle" src="https://github.com/user-attachments/assets/fbcef073-612e-4678-9ee3-320560416e58" />


# 🔒 Explain of TeaSpeak Order Placement
<img width="1653" height="714" alt="Untitled-2025-10-22-2359" src="https://github.com/user-attachments/assets/7d2847ca-5cb3-4a85-9f0a-da8dc840a8b8" />
