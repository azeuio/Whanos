#! /usr/bin/env python3
import os
import sys

def find_file(path, file_name):
    for root, dirs, files in os.walk(path):
        if file_name in files:
            return os.path.join(root, file_name)

def multiple_lang(path):
    lang_file = ['requirements.txt',
    'package.json', 'Makefile', 'pom.xml', 'main.bf']
    lang_detected = 0

    for lang in lang_file:
        if find_file(path, lang):
            lang_detected += 1
    return lang_detected

def find_lang(path):
    detection_file = [('requirements.txt', 'python'),
    ('package.json', 'javascript'), ('Makefile', 'c'),
    ('pom.xml', 'java'), ('main.bf', 'befunge')]
    lang_found = 0

    for file_name, lang in detection_file:
        directory = path
        if (lang == 'java' or lang == 'befunge'):
            directory += '/app'
        if find_file(directory, file_name):
            return lang
    return ''

def detect_lang(path):
    lang = find_lang(path)
    if multiple_lang(path) > 1:
        raise Exception("Multiple languages in one directory")
    print(lang)
    return 0

args = sys.argv[1:]
directory = args[0]
try:
    detect_lang(directory)
except Exception as error:
    print("Error: " + repr(error))