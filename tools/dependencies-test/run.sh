 #!/bin/bash
 
 mvn dependency:tree
 mvn dependency:purge-local-repository
 mvn -e exec:java -Dexec.mainClass="xy.lib.theme.ThemeEqualizerDialog" -Dexec.args=""
 