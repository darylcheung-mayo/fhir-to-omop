:: -----------------------
::
:: bat file to install and build projects for WebAPI and Atlas
::   TODO: NEED TO ADD TAG AND CHECKOUT FOR fhir-to-omop
::
:: -----------------------

@echo off

::
:: Clone fhir-to-omop
::

call jv 11

echo.
echo.
echo Cloning fhir-to-omop
rmdir fhir-to-omop
git clone https://github.com/NACHC-CAD/fhir-to-omop
cd fhir-to-omop
call mvn clean 
call mvn install
echo Done with fhir-to-omop mvn clean install 
cd ..
echo.
echo.

call jv 8

::
:: Clone WebAPI
::

echo.
echo.
echo Cloning WebAPI
rmdir WebAPI
git clone https://github.com/OHDSI/WebAPI
cd WebAPI
git checkout v2.12.0
echo.
echo.
mkdir WebAPIConfig
copy ..\fhir-to-omop\docs\ohdsi-tools\webapi\WebAPIConfig\settings.xml .\WebAPIConfig
call mvn clean   -DskipUnitTests -DskipITtests -s WebAPIConfig/settings.xml -P webapi-postgresql
call mvn package -DskipUnitTests -DskipITtests -s WebAPIConfig/settings.xml -P webapi-postgresql
cd ..

::
:: Clone and build atlas
::

echo Cloning Atlas
rmdir atlas 
git clone https://github.com/OHDSI/Atlas
rename Atlas atlas
cd atlas
git checkout v2.12.0
call npm run build
cd ..
echo.
echo.

::
:: Move WebAPI and atlas to tomcat webapps folder
::

echo.
echo.
echo Moving web apps (WebAPI and atlas) to tomcat webapps directory
copy .\WebAPI\target\WebAPI.war .\..\servers\apache-tomcat\apache-tomcat-8.5.85-windows-x64\apache-tomcat-8.5.85\webapps
robocopy .\atlas .\..\servers\apache-tomcat\apache-tomcat-8.5.85-windows-x64\apache-tomcat-8.5.85\webapps\atlas /E 
echo Done moving web app files. 

::
:: Set current version of java to 11
::

echo Setting current version of java to Java 11
call jv 11
echo Done setting Java version.  

::
:: Done!
::

echo ---------------------
echo Done.
echo ---------------------
echo.
echo.

