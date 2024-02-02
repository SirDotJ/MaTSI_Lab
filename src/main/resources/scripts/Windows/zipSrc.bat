cd /d "out/artifacts/FullApplication_Windows"
del "src.zip"
cd "src"
zip -r "../src.zip" ./* -x "jdk/*"