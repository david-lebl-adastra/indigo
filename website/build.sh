#!/bin/bash

# TODO
# Auto update the demos?

set -e

WEBSITE_DIR=$(pwd)
PROJECT_DIR=$WEBSITE_DIR/..
INDIGO_ENGINE_DIR=$PROJECT_DIR/indigo/

# -----
# generate / check docs
#  - "indigo/doc" // Docs in ./indigo/target/scala-3.0.2/api/
#  - "docs/mdoc"  // Docs in ./indigo/indigo-docs/target/mdoc
cd $INDIGO_ENGINE_DIR
sbt gendocs
cd $WEBSITE_DIR

# -----
# build the site
rm -fr $WEBSITE_DIR/static/api
mkdir -p $WEBSITE_DIR/static/api

cp -R $INDIGO_ENGINE_DIR/indigo/target/scala-3.*/api/ $WEBSITE_DIR/static/api

yarn run build

# -----
# move to publish
rm -fr $PROJECT_DIR/docs
mkdir $PROJECT_DIR/docs
cp -R $WEBSITE_DIR/build/indigo-site/* $PROJECT_DIR/docs/
