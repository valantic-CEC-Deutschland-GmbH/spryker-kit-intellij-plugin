#!/bin/bash
# rm -rf templates/*
# git clone -b feature/templates-lite git@github.com:nexusunited/spryk.git spryk

# mv spryk/config/spryk/templates/* templates

# rm -rf spryk

# find themes/templates -type f -exec sed -i 's/{{ /{$/g' {} + && find . -type f -exec sed -i 's/}/}/g' {} +

tree -J templates/ > template-map.json
