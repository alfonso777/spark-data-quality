#!/bin/bash

if [ $# -lt 1 ]; then
  echo "Argumentos incorretos!. Argumentos esperados: <database> [tables]>"
  echo "As tabelas são opcionais e devem ir separadas por vírgula"
  exit 1
fi

VERSION=1.1.0
APP_NAME="DATAQUALITY-$VERSION"
JAR_NAME=filtro-next-boletos-assembly-$VERSION.jar

DATABASE=$1
TABLES=$2
if [ -z "$2" ]; then
  echo "Tables were not defined. All tables will be considered "
  TABLES="ALL"
fi

MASTER=local[*]
DEPLOYMODE=client
EXECUTOR_CORES=3
NUM_EXECUTORS=3
EXECUTOR_MEMORY=4G
DRIVER_MEMORY_OVERHEAD=1024
EXECUTOR_MEMORY_OVERHEAD=1024

spark-submit --verbose  \
           --name "$APP_NAME" \
           --master $MASTER \
           --deploy-mode $DEPLOYMODE \
           --num-executors $NUM_EXECUTORS \
           --executor-cores $EXECUTOR_CORES \
           --executor-memory $EXECUTOR_MEMORY \
           --conf spark.yarn.driver.memoryOverhead=$DRIVER_MEMORY_OVERHEAD \
           --conf spark.yarn.executor.memoryOverhead=$EXECUTOR_MEMORY_OVERHEAD \
           $JAR_NAME $DATABASE $TABLES