export PATH=$PATH:/usr/local/development/axis2-1.4.1/bin/
rm ./src/main/java/eu/atosresearch/seiplab/yourbpm/dtc/client/stub/*.java
wsdl2java.sh -uri ./DTComposer.wsdl -p eu.atosresearch.seiplab.yourbpm.dtc.client.stub -d adb -s -S ./src/main/java
