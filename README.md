# Markdown Converter

This console application is made for converting markdown formatted text to HTML or ANSI.

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
### Write to the CLI
To use the application you should pass the path to your input text (or .md) file as a parameter:

```
java -jar .\target\MarkdownToHtmlConverter.jar <input_file>
```

This will print the output to the CLI in an ANSI format by default.

### Write to a file
If you want your output to appear in a specific file, you can also pass `--out` parameter with the path to your output file:

```
java -jar .\target\MarkdownToHtmlConverter.jar <input_file> [--out <output_file>]
```
The output will be in an HTML format by default

### Define output format
If you want to specify the output format you can pass `--format` argument:

```
java -jar .\target\MarkdownToHtmlConverter.jar <input_file> [--out <output_file>] [--format=ansi/html]
```
- `--format` argument may be either `html` or `ansi`.
- For the **console output**, `--format` should be passed right after your input file path
- For the **file output**, `--format` should be passed right after your output file path

### Errors
If the application finds any syntax errors, such as nested or unclosed formatting, 
it will print out an error message, containing the part of incorrect text.

## How to run tests
To run the tests you should use a dedicated command in a project root:
```
mvn test
```
You can also specify which exact test file you want to run:
```
mvn test -Dtest=MarkdownToAnsiConverterTest
```
and even specify the method:
```
mvn test -Dtest=MarkdownToAnsiConverterTest#boldTextIsConvertedRight
```

## Revert commit

Commit `5b78f25428edff2958243a3d120881ac30e62d4d`

reverts `bad0d7c9c60a58bbea30810f1047275a204b591a`

## Failed tests commit

`5b93686c18644e05e916567037957b24d42d49f7`

## Conclusions

Отож, виконуючи цю лабораторну роботу, я спробував використання unit-тестів при розробці програмного забезпечення 
і хочу зазначити, що це насправді дуже корисна практика.

Написання тестів зекономило мені час на етапі розширення функціоналу і фіксу багів.
Після кожної зміни в коді достатньо було перезапустити тести, аби переконатись у тому, що попередній функціонал
не зламався, а новий працює коректно (замість перевірки всього вручну).

Зручним є те, що нам не обов'язково тестувати весь код, а достатньо конкретного модуля 
(тому цей тип тестів так і називається).

Також було налаштовано CI через GitHub Actions, що додає додаткової впевненості у 
валідності кожного комміта і спрощує орієнтування у змінах при командній розробці.