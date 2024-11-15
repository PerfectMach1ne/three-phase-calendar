#!/bin/bash

javac --enable-preview --release 21 Main.java
java --enable-preview Main Main.class
