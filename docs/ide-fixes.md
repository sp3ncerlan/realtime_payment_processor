IntelliJ / IDE fixes for realtime_payment_processor

Common problems & fixes

1) Color coding / syntax highlighting missing
- Ensure Project SDK is set to Java 17 (Spring Boot uses Java 17 here).
  - File > Project Structure > Project SDK -> select Java 17

2) Lombok / generated annotations not recognized
- Install Lombok plugin:
  - File > Settings > Plugins > Marketplace -> search "Lombok" -> Install -> Restart
- Enable annotation processing:
  - File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors -> check "Enable annotation processing"

3) Spring symbols not resolved
- Ensure Maven project is imported and reloaded:
  - View > Tool Windows > Maven -> click "Reload All Maven Projects"
- Enable Spring support if required:
  - Tools > Spring > Enable Spring support

4) Stale caches / strange errors
- File > Invalidate Caches / Restart -> Invalidate and Restart

5) Terminal build for verification (Windows PowerShell)
- Run the backend tests and build to confirm things compile:

  cd backend; .\mvnw.cmd clean test -U

6) If annotation colors aren't yellow
- IntelliJ applies different colors depending on the language level and plugins. After enabling Lombok & annotation processors, restart the IDE and reimport the project. If problems persist, check File > Settings > Editor > Color Scheme > Java to verify annotation colors.

If the project still behaves strangely after these steps, collect the IDE logs (Help > Show Log in Explorer) and attach them when requesting help.

