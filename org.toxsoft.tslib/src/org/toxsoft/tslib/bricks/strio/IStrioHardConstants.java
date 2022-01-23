package org.toxsoft.tslib.bricks.strio;

/**
 * Unhcangeable constants for string i/o formats handling.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public interface IStrioHardConstants {

  /**
   * End of stream (file).
   * <p>
   * Used as return value of single character reader methods.
   * <p>
   * According to Unicode standard, this value:
   * <ul>
   * <li>is used only inside program codes, never for data transmission or storage;</li>
   * <li>does not corresponds to any character.</li>
   * </ul>
   */
  char CHAR_EOF = 0xFFFF;

  // TRANSLATE

  /**
   * Экранирующий символ - выделяет специальные символы в строке-в-кавычках.
   */
  char CHAR_ESCAPE = '\\';

  /**
   * Quote character used to enclose string=in-quotes.
   */
  char CHAR_QUOTE = '"';

  /**
   * Space charachter (' ').
   */
  char CHAR_SPACE = ' ';

  /**
   * Equal sign '='.
   */
  char CHAR_EQUAL = '=';

  /**
   * Default line comments starting charachter.
   */
  char CHAR_LINE_COMMENT_SHELL = '#';

  /**
   * Universal end of line charachter of strio subsystem.
   */
  char CHAR_EOL = '\n';

  /**
   * Set starting charachter.
   */
  char CHAR_SET_BEGIN = '{';

  /**
   * Set end charachter.
   */
  char CHAR_SET_END = '}';

  /**
   * Elements delimiter charachter in lists and arrays.
   */
  char CHAR_ITEM_SEPARATOR = ',';

  /**
   * Array start charachter.
   */
  char CHAR_ARRAY_BEGIN = '[';

  /**
   * Array end charachter.
   */
  char CHAR_ARRAY_END = ']';

  /**
   * Embedded value object storage prefix.
   */
  char CHAR_VALOBJ_PREFIX = '@';

  // ------------------------------------------------------------------------------------
  // Time moment representation as "YYYY-MM-DDTHH:MM:SS.mmm"
  //

  /**
   * В метке времени разделитель между год-месяц-день.
   */
  char CHAR_TIMESTAMP_YMD_SEPARATOR = '-';

  /**
   * В метке времени разделитель между датой и временем суток.
   */
  char CHAR_TIMESTAMP_DATETIME_SEPARATOR = '_';

  /**
   * В метке времени разделитель между час:минуты:секунды.
   */
  char CHAR_TIMESTAMP_HMS_SEPARATOR = ':';

  /**
   * В метке времени разделитель между секндами и миллисекундами.
   */
  char CHAR_TIMESTAMP_MILLISEC_SEPARATOR = '.';

  /**
   * Формат представления метки времени (с точностью до миллисекунд).
   * <p>
   * Используется следующий формат фиксированной длины <br>
   * <b>"YYYY-MM-DD_hh:mm:ss.iii"</b>,<br>
   * где:
   * <ul>
   * <li><b>YYYY</b> - год (0000..9999);</li>
   * <li>символы тире '<b>-</b>' - ({@link #CHAR_TIMESTAMP_YMD_SEPARATOR}) разделители в дате;</li>
   * <li><b>MM</b> - месяц года (01..12);</li>
   * <li><b>DD</b> - день месяца (01..31);</li>
   * <li>символ подчеркивания '<b>_</b>' - ({@link #CHAR_TIMESTAMP_DATETIME_SEPARATOR}) разделитель между датой и
   * временем суток;</li>
   * <li><b>hh</b> - час дня (00..23);</li>
   * <li>символы двоеточие '<b>:</b>' - ({@link #CHAR_TIMESTAMP_HMS_SEPARATOR}) разделители во времени суток (часы,
   * минуты, секунды);</li>
   * <li><b>mm</b> - минута в часе (00..59);</li>
   * <li><b>ss</b> - секунда в минуте (00..59);</li>
   * <li>символ точка '<b>.</b>' - ({@link #CHAR_TIMESTAMP_MILLISEC_SEPARATOR}) разделитель между секунадми и
   * миллисекундами;</li>
   * <li><b>iii</b> - миллисекунды в секунде (000..999).</li>
   * </ul>
   * Сокращенные записи:
   * <ul>
   * <li>только дата в виде "YYYY-MM-DD" ({@link #ONLYDATE_FMT});</li>
   * <li>дата и время суток с точностью до секунды YYYY-MM-DD_HH:MM:SS".</li>
   * </ul>
   */
  String TIMESTAMP_FMT = "YYYY-MM-DD_HH:MM:SS.mmm"; //$NON-NLS-1$

  /**
   * Запись метки времени в виде только даты.
   */
  String ONLYDATE_FMT = "YYYY-MM-DD"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // General constants
  //

  /**
   * Символы пробелов в виде строки.
   * <p>
   * Следует отличать понятие символов-<b>пропусков</b> и символов-<b>пробелов</b>. пробелы это общепринятые
   * непечатаемые символы, в частности собственно пробел и символ табуляции. Пропуски - это символы, которые обычно
   * игнорируются как и пробелы. Пропуски включат в себя пробелы, и добавляют к ним символы типа перевода строки (т.е.
   * конца строки, начала новой строки).
   * <p>
   * Кроме пробелов и пропусков, существуют символы-<b>разделители</b>. По аналогии с пропусками, это символы, которые
   * разделют лексемы в текстовом потоке. Разделители включают в себы пропуски (и естественно и пробелы), и добавляют к
   * ним многие символы, в том числе начала и окочания массивов и списков.
   * <p>
   * Напомним, что в качестве прототипа текстового хранения данных используется синтаксис C/C++ и Java.
   */
  String DEFAULT_SPACE_CHARS = " \t";

  /**
   * Пропуски, игнорируемые при чтении (по умолчанию).
   * <p>
   * Понятие пробелов, пропусков и разделителей описано в комментарии к {@link #DEFAULT_SPACE_CHARS}.
   */
  String DEFAULT_BYPASSED_CHARS = DEFAULT_SPACE_CHARS + "\n\r";

  /**
   * Символы - разделители лексем входного потока (по умолчанию).
   * <p>
   * Понятие пробелов, пропусков и разделителей описано в комментарии к {@link #DEFAULT_SPACE_CHARS}.
   */
  String DEFAULT_DELIMITER_CHARS = DEFAULT_BYPASSED_CHARS + ",{}[];:=()<>";

  // ------------------------------------------------------------------------------------
  // Boolean names
  //

  /**
   * Boolean <code>true</code> text stored by {@link IStrioWriter#writeBoolean(boolean)} and resd by
   * {@link IStrioReader#readBoolean()}.
   */
  String STR_BOOLEAN_TRUE = "true";

  /**
   * Boolean <code>false</code> text stored by {@link IStrioWriter#writeBoolean(boolean)} and resd by
   * {@link IStrioReader#readBoolean()}.
   */
  String STR_BOOLEAN_FALSE = "false";

}
