set PATH=%PATH%;D:\<path_to_axis>\bin\
set AXIS2_HOME=D:\<path_to_axis>
del .\src\main\java\eu\atosresearch\seiplab\yourbpm\dtc\client\stub\*.java
wsdl2java.bat -uri .\DTComposer.wsdl -p eu.atosresearch.seiplab.yourbpm.dtc.client.stub -d adb -s -S .\src\main\java
