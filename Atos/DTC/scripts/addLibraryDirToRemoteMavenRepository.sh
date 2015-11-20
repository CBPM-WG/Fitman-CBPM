#mvn install:install-file -DgroupId=jena -DartifactId=antlr -Dversion=2.7.5 -Dpackaging=jar -Dfile=lib/Jena-2.5.7/antlr-2.7.5.jar

#!/bin/sh

if [ $# != 3 ]
then
  echo "usage: addLibraryDirToMavenRepository.sh <library_dir> <groupId> <version> <repositoryId> <repositoryUrl>"
  exit 1
fi

current_dir=`pwd`
cd $1

for file in `dir -d *` ; do
 echo "mvn install:install-file -DgroupId=$2 -DartifactId=$file -Dversion=$3 -Dpackaging=jar -Dfile=$file"
 mvn deploy:deploy-file -Dfile=$file -DgroupId=$2 -DartifactId=$file -Dversion=$3 -Dpackaging=jar -DrepositoryId=$4 -Durl=$5
done

cd $current_dir
