#!/bin/bash

FILES_CMD=(git ls-files -z -- '*pom.xml' '*.java' '*.casm' '*.coreasm' '*.css')

#MATCH=(-not \( -path '*/target/*' -or -name '.*' \) -and \( -name '*.java' -or -name 'pom.xml' \))

# remove trailing whitespaces
"${FILES_CMD[@]}" | xargs -0 -I {} sh -c "sed -i 's/[ \t]*$//' \"{}\""

# ensure newline at EOF
"${FILES_CMD[@]}" | xargs -0 -I {} sh -c "sed -i -e '\$a\\' \"{}\""

# remove multiple newlines at EOF
"${FILES_CMD[@]}" | xargs -0 -I {} sh -c "sed -i -e :a -e '/^\n*$/{\$d;N;};/\n$/ba' \"{}\""

## expand tab to two spaces
#find . -type f "${MATCH[@]}" ! -type d -exec bash -c 'expand -t 2 "$0" > /tmp/e && mv /tmp/e "$0"' {} \;

## expand tab to four spaces
#find . -type f "${MATCH[@]}" ! -type d -exec bash -c 'expand -t 4 "$0" > /tmp/e && mv /tmp/e "$0"' {} \;

# change four spaces to tab
#find . -type f -"${MATCH[@]}" ! -type d -exec bash -c 'unexpand --first-only -t 4 "$0" > /tmp/e && mv /tmp/e "$0"' {} \;
#"${FILES[@]}" | xargs -0 unexpand --first-only -t 4 "$0" > "$0"-tab && mv "$0"-tab "$0"
"${FILES_CMD[@]}" | xargs -0 -I {} sh -c "unexpand --first-only -t 4 \"{}\" > \"{}\"-tab && mv \"{}\"-tab \"{}\""


# find unexpected encodings
#find . -type f "${MATCH[@]}" -exec file --mime {} \;  | grep -v 'utf-8\|binary\|ascii'
"${FILES_CMD[@]}" | xargs -0 -I {} sh -c "file --mime \"{}\" | grep -v 'utf-8\|binary\|ascii'"
