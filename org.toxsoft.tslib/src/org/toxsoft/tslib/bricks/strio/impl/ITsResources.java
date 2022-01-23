package org.toxsoft.tslib.bricks.strio.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  // ------------------------------------------------------------------------------------
  // en.EN
  //

  String MSG_ERR_IO_EXCEPTION               = "Input/output exception (IOException) happaned";
  String MSG_ERR_STRIO_EXCEPTION            = "Invalid format of textual representation";
  String FMT_ERR_HEX_CHAR_EXPECTED          = "Character '%c' found instead of hexadecimal digit (0-9,a-f,A-F)";
  String FMT_ERR_LEFT_BRACKET_EXPECTED      = "Instead of character %c left bracket { or [ was expected ";
  String FMT_ERR_PUTING_BACK_TOO_MANY_CHARS = "Attempt to return back more than %d characters";
  String MSG_ERR_PUTING_BACK_UNREAD_CHAR    = "Attempt to return back unread character";
  String MSG_ERR_UNEXPECTED_EOF             = "Enexpected end of input data";
  String MSG_ERR_INV_DEC_DIGIT              = "Digit (0..9) was expected";
  String MSG_ERR_INV_HEX_DIGIT              = "Hex digit (0..9, a..f, A..F) was expected";
  String MSG_ERR_INV_TIMESTAMP              = "Invalid date (timestamp)";
  String FMT_ERR_INV_BOOL_NAME              = "Boolean value name was expected instead of '%s'";
  String FMT_ERR_EOF_INSETEAD_CH            = "Character '%c' was expected instead of EOF";
  String FMT_ERR_CHAR_EXPECTED              = "Instead of character '%c' character '%c' was expected";
  String FMT_ERR_STRING_EXPECTED            = "text representation format violation, '%s' was expected";
  String MSG_ERR_TOO_BIG_INT                = "Integer is out of 32-bit number range";
  String MSG_ERR_TOO_BIG_LONG               = "Integer is out of 64-bit number range";
  String MSG_ERR_INV_INTEGER                = "Invalid integer number";
  String MSG_ERR_INV_FLOATING_NUM           = "Invalid floating number";
  String FMT_ERR_QUOTE_EXPECTED             = "Quote '\"' was expected instead of '%c' character";
  String FMT_ERR_INV_ID_PATH_START          = "IDname (IDpath) can't start with character '%c'";
  String MSG_ERR_CANT_READ_ID_NAME          = "IDname was expected";
  String MSG_ERR_CANT_READ_ID_PATH          = "IDpath was expected";
  String MSG_ERR_INV_ARRAY_NEXT             = "Array end or next item was expected";
  String MSG_ERR_INV_SET_NEXT               = "Set end or next item was expected";
  String FMT_ERR_DUPLICATE_SECTION          = "Duplicate section %s ecountered";
  String FMT_ERR_INV_SECT_CONTENT1          = "Invalid section content - must end with ']' char";
  String FMT_ERR_INV_SECT_CONTENT2          = "Invalid section content - must end with '}' char";
  String MSG_ERR_INV_SECT_CONTENT3          = "Invalid section content - must be enclosed between [..] or {..}";

  // ------------------------------------------------------------------------------------
  // ru.RU
  //

  // String MSG_ERR_IO_EXCEPTION = "Возникла ошибка ввода/вывода IOException";
  // String MSG_ERR_STRID_EXCEPTION = "Неверный формат текстового представления";
  // String FMT_ERR_HEX_CHAR_EXPECTED = "Вместо символа 16-тиричного числа (0-9,a-f,A-F) встретился символ '%c'";
  // String FMT_ERR_LEFT_BRACKET_EXPECTED = "Вместо символа %c ожидалась левая скобка { или [";
  // String FMT_ERR_PUTING_BACK_TOO_MANY_CHARS = "Попытка вернуть в входной поток более %d символов";
  // String MSG_ERR_PUTING_BACK_UNREAD_CHAR = "Попытка вернуть еще непрочитанный символ";
  // String MSG_ERR_UNEXPECTED_EOF = "Неожиданное окончание входных данных";
  // String MSG_ERR_INV_DEC_DIGIT = "Ожидалась цифра (0..9)";
  // String MSG_ERR_INV_HEX_DIGIT = "Ожидалась 16-ричная цифра (0..9, a..f, A..F)";
  // String MSG_ERR_INV_TIMESTAMP = "Неверный формат даты (метки времени)";
  // String FMT_ERR_EOF_INSETEAD_CH = "Вместо конца файла ожидался символ '%c'";
  // String FMT_ERR_CHAR_EXPECTED = "Вместо символа '%c' ожидался символ '%c'";
  // String FMT_ERR_STRING_EXPECTED = "Нарушение формата, ожидалось '%s'";
  // String FMT_ERR_INV_BOOL_NAME = "Ожидалось название логического значения, а встретилось '%s'";
  // String MSG_ERR_TOO_BIG_INT = "Считываемое число выходит за пределы 32-битного знакового целого";
  // String MSG_ERR_TOO_BIG_LONG = "Считываемое число выходит за пределы 64-битного знакового целого";
  // String MSG_ERR_INV_INTEGER = "Неверный формат целого числа";
  // String MSG_ERR_INV_FLOATING_NUM = "Неверный формат числа";
  // String FMT_ERR_QUOTE_EXPECTED = "Вместо символа ('%c') ожидалась двойная кавычка (символ '\"')";
  // String MSG_ERR_INV_ARRAY_NEXT = "Ожидалось продолжение или окончание массива";
  // String MSG_ERR_INV_SET_NEXT = "Ожидалось продолжение или окончание набора";
  // String FMT_ERR_DUPLICATE_SECTION = "Дублированный раздел %s";

}
