# برق لايف ⚡ (BarqLive)

تطبيق Android (Kotlin + Jetpack Compose) لعرض مباريات كرة القدم وتشغيل البث، بواجهة داكنة كهرمائية (سماوي/بنفسجي/ليموني نيون) وخط Cairo.

## التشغيل
1. افتح المجلد فـ Android Studio (Ladybug أو أحدث) وخلّيه يعمل Sync.
2. نزّل خط **Cairo** من Google Fonts (مجلد static) وحط هاد الملفات فـ `app/src/main/res/font/` بهاد الأسماء بالضبط:
   - `cairo_regular.ttf`
   - `cairo_bold.ttf`
   - `cairo_black.ttf`
3. سجّل فـ https://www.football-data.org وجيب المفتاح المجاني، وزيدو فـ `local.properties`:
   ```
   FOOTBALL_API_KEY=مفتاحك
   ```
4. شغّل على محاكي أو هاتف.

## البث
`StreamSource.urlFor()` فـ `data/FootballApi.kt` كيرجع حاليا فيديو تجريبي عمومي. بدّلو برابط HLS (.m3u8) اللي عندك حق بثه (ترخيص من مالك الحقوق). بث المباريات بدون ترخيص ممنوع قانونيا وكيمكن يتحيّد التطبيق من Google Play.

## البنية
- `data/` : الموديلات + استدعاء API + مصدر البث
- `ui/Theme.kt` : الألوان والخط
- `ui/HomeScreen.kt` : بطاقات المباريات (توهج، نقطة "مباشر" نابضة)
- `ui/PlayerScreen.kt` : مشغل Media3 ExoPlayer
