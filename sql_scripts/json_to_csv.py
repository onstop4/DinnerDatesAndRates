#!/usr/bin/env python3
import csv
import json
import os

DIRECTORY_NAME = "tables"

directory_path = os.path.join(
    os.path.dirname(os.path.realpath(__file__)), DIRECTORY_NAME
)


def write_csv_to_file(contents, file):
    if len(contents) > 0:
        writer = csv.writer(
            file, delimiter=",", quotechar='"', quoting=csv.QUOTE_MINIMAL
        )
        writer.writerow(list(contents[0].keys()))
        writer.writerows(list(value.values()) for value in contents)


for filename in os.listdir(directory_path):
    file_path = os.path.join(directory_path, filename)

    if file_path.lower().endswith(".json") and os.path.isfile(file_path):
        with open(file_path) as file:
            contents = json.load(file)

        contents = list(contents.values())[0]
        with open(os.path.splitext(file_path)[0] + ".csv", "w") as file:
            write_csv_to_file(contents, file)
