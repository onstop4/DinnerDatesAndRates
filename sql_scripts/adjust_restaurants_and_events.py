#!/usr/bin/env python3
import os.path
import re

FILENAME = "restaurants_and_events.sql"

CREATE_TABLE_REGEX = "CREATE TABLE(?! IF NOT EXISTS)"
CREATE_TABLE_SUBST = "CREATE TABLE IF NOT EXISTS"

AUTO_INCREMENT_REGEX = r" AUTO_INCREMENT=\d+"

BEGINNING = """-- Create and use dinnerdates database

CREATE DATABASE IF NOT EXISTS dinnerdates;
USE dinnerdates;


"""

file_path = os.path.join(os.path.dirname(os.path.realpath(__file__)), FILENAME)

with open(file_path, "r+") as file:
    contents = file.read()

    if not contents.startswith("-- Create and use dinnerdates database"):
        contents = BEGINNING + contents

    contents = re.sub(CREATE_TABLE_REGEX, CREATE_TABLE_SUBST, contents, 0)

    contents = re.sub(AUTO_INCREMENT_REGEX, "", contents, 0)

    file.seek(0)
    file.write(contents)
