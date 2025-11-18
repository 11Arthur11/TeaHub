## 🔐 Authentication API

| Method | Endpoint                | Request Body (JSON)                                                                         | Description                                                                          |
|:--------|:------------------------|:--------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------|
| `POST` | `/v1/auth/init`         | `{"phoneNumber": "+989053781183"}`                                                          | شروع احراز هویت سایت (نقطه ورود)                                                     |
| `POST` | `/api/v1/auth/login`    | `{ "twoFactorCode": "string", "rememberMe": boolean }`                                      | ارسال فرم لاگین در صورت وجود کاربر و داشتن کوکی دو عاملی و ورود کد ارسال شده به تلفن |
| `POST` | `/api/v1/auth/register` | `"{"twoFactorCode": "string","firstName": "string","lastName": "string","email": "string"}` | ثبت نام کاربر در صورت داشتن کوکی و توکن phone verify و کد تایید                      |