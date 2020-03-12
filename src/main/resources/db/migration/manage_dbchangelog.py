import os, sys, getopt, re, boto3, pymysql

commentStart   = '^\W*\<\!\-\-\W*$'
commentEnd     = '^\W*\-\-\>\W*$'
includeLine    = '^\W*\<\include file\=\"([\w\/\.\"])+\>\W*$'
migrateVersion = '(V\d+_\d+)'


def main(argv):
    input_file = ''
    version = ''

    try:
        opts, args = getopt.getopt(argv,"hi:v:",["input_file=","version="])
    except getopt.GetoptError:
        print_usage()
        sys.exit(2)

    for opt, arg in opts:
        if opt == '-h':
            print_usage()
            sys.exit()
        elif opt in ("-i", "--input_file"):
            input_file = arg
        elif opt in ("-v", "--version"):
            version = arg

    if ((len(input_file) == 0)):
        print_usage()
        sys.exit(2)

    process(input_file, version)

def print_usage():
    print('usage: manage_dbchangelog.py -i <input_file> -v <version>')

def process(input_file, version):

    output_file_lines = []

    try:
        reCommentStart   = re.compile(commentStart)
        reCommentEnd     = re.compile(commentEnd)
        reIncludeLine    = re.compile(includeLine)
        with open(input_file, 'r') as inputfile:
            for line in inputfile:
                if (not(reCommentStart.match(line) or reCommentEnd.match(line))):
                    commentThisLine = False
                    if reIncludeLine.match(line):
                        regexGroups = re.search(migrateVersion, line)
                        commentThisLine = regexGroups.group(0) > version
                    if (commentThisLine):
                        output_file_lines.append('<!--\n')
                        output_file_lines.append(line)
                        output_file_lines.append('-->\n')
                    else:
                        output_file_lines.append(line)
    except:
        print("ERROR: Unexpected error with file at " + input_file + ".")
        sys.exit()

    inputfile.close()
    with open(input_file, 'w') as outputfile:
        outputfile.writelines(output_file_lines)
    outputfile.close()

if __name__ == "__main__":
    main(sys.argv[1:])