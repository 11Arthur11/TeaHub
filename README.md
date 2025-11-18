## 🔐 Authentication API

| Method | Endpoint                | Request Body (JSON)                                                                         | Description                                                                          |
|:--------|:------------------------|:--------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------|
| `POST` | `/v1/auth/init`         | `{"phoneNumber": "+989053781183"}`                                                          | شروع احراز هویت سایت (نقطه ورود)                                                     |
| `POST` | `/api/v1/auth/login`    | `{ "twoFactorCode": "string", "rememberMe": boolean }`                                      | ارسال فرم لاگین در صورت وجود کاربر و داشتن کوکی دو عاملی و ورود کد ارسال شده به تلفن |
| `POST` | `/api/v1/auth/register` | `"{"twoFactorCode": "string","firstName": "string","lastName": "string","email": "string"}` | ثبت نام کاربر در صورت داشتن کوکی و توکن phone verify و کد تایید                      |

# 🍃 Explain of whole concept of Spring & Spring Security
<img width="2175" height="1226" alt="Untitled-2025-10-22-2359" src="https://github.com/user-attachments/assets/c3034c6f-6ed1-43a6-b315-2a158b372314" />

# 🔒 Explain of Jwt Authentication Filter LifeCycle
<img width="1831" height="1283" alt="jwt lifecycle" src="https://github.com/user-attachments/assets/fbcef073-612e-4678-9ee3-320560416e58" />
