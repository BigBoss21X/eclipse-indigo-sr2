#!/bin/bash
ls *.html | sed 's/.html/=/' > commands.properties
