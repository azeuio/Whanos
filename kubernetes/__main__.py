#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# Run with python3 -m kubernetes
# This script takes a partial kubernetes file (whanos.yml on the pdf)
# then adds its info to the template file to finally create 'whanos-full.yaml'
import yaml
import sys
import argparse

def main(template_file, partial_kube_file):
    with open(partial_kube_file, 'r') as stream:
        try:
            kube = yaml.safe_load(stream)
        except yaml.YAMLError as exc:
            print(exc)
            sys.exit(1)
    with open(template_file, 'r') as stream:
        try:
            template = yaml.safe_load(stream)
        except yaml.YAMLError as exc:
            print(exc)
            sys.exit(1)

    if len(kube) > 1 or ('deployment' not in kube and len(kube) == 1):
        print('whanos.yml should only have one key (deployment)', file=sys.stderr)
        sys.exit(1)
    if not kube['deployment']:
        sys.exit(0)
    if 'replicas' in kube['deployment']:
        template['spec']['replicas'] = kube['deployment']['replicas']
    if 'resources' in kube['deployment']:
        for resource in kube['deployment']['resources']:
            if template['spec']['template']['spec']['containers'][0].get('resources', None) is None:
                template['spec']['template']['spec']['containers'][0]['resources'] = dict()
            template['spec']['template']['spec']['containers'][0]['resources'][resource] = kube['deployment']['resources'][resource]
    if 'ports' in kube['deployment']:
        template['spec']['template']['spec']['containers'][0]['ports'] = []
        for i, port in enumerate(kube['deployment']['ports']):
            template['spec']['template']['spec']['containers'][0]['ports'].append(dict())
            template['spec']['template']['spec']['containers'][0]['ports'][i]['containerPort'] = port

    with open('whanos-full.yaml', 'w') as outfile:
        yaml.dump(template, outfile, default_flow_style=False)

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Create a kubernetes cluster from a whanos cluster')
    parser.add_argument('partial_kube_file', metavar='FILE', type=str,
                        help='The partial kubernetes file to use')
    parser.add_argument('template_file', metavar='TEMPLATE_FILE', type=str,
                        help='The kubernetes file to add the partial kube file info to')
    namespace = parser.parse_args()
    main(namespace.template_file, namespace.partial_kube_file)