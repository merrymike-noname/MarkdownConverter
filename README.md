# Markdown to HTML Converter

This console application is made for converting formatting in markdown to HTML tags.

## Build and deploy:

1. Download project
2. Open your CLI
3. Navigate to root project folder
4. Run command:

```
mvn clean package
```
5. Execute the built jar file:

```
java -jar .\target\MarkdownToHtmlConverter.jar
```

## Usage

To use the application you should pass the path to your input text (or .md) file as a parameter:

```
java -jar .\target\MarkdownToHtmlConverter.jar <input_file>
```

This will print the output to the CLI.

If you want your output to appear in a specific file, you can also pass `--out` parameter with the path to your output file:

```
java -jar .\target\MarkdownToHtmlConverter.jar <input_file> [--out <output_file>]
```

If the application finds any syntax errors, such as nested or unclosed formatting, it will print out an error message, containing the part of incorrect text.

## Revert commit

Commit `5b78f25428edff2958243a3d120881ac30e62d4d`

reverts `bad0d7c9c60a58bbea30810f1047275a204b591a`