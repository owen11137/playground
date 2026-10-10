# Playground — آزمایشگاه زمان‌بندی سفارش

پروژه مستقل React + Material UI و Spring Boot برای شبیه‌سازی سفارش زمان‌دار روی ویندوز.
هیچ اتصالی به کارگزاری یا سفارش واقعی ندارد.

## اجرا روی ویندوز
Java 17+، Maven 3.6.3+ و Node.js 22.12+ لازم است.
دو ترمینال PowerShell باز کنید:
```powershell
cd backend
mvn spring-boot:run
```
```powershell
cd frontend
npm ci
npm run dev
```
در مرورگر http://localhost:5173 را باز کنید. بک‌اند روی 8080 است.
نماد، قیمت به ریال، تعداد و زمان محلی ویندوز را تنظیم و ثبت آزمایشی را بزنید.
سرور هر ۱۰۰ میلی‌ثانیه سررسید را بررسی می‌کند؛ این شبیه‌ساز تضمین دقت زمان یا جایگاه صف واقعی ندارد.
دکمه توقف همه سفارش‌های در انتظار را لغو می‌کند. شناسه درخواست از ثبت تکراری جلوگیری می‌کند.
زمان API به UTC و نمایش پنل به وقت سیستم است.
سقف آزمایشی یک میلیارد ریال، حداکثر ۱۰۰۰ سفارش در نشست و ۲۴ ساعت زمان‌بندی است.
اطلاعات در حافظه‌اند و با توقف برنامه حذف می‌شوند. این برنامه فقط برای استفاده محلی است.

## بررسی
```powershell
cd backend
mvn test
```
```powershell
cd frontend
npm run build
```
## API
GET /api/simulation/orders
POST /api/simulation/orders : symbol, price, quantity, executeAt (ISO UTC), requestId
POST /api/simulation/stop
