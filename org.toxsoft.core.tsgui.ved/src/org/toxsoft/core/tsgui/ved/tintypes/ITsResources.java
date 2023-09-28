package org.toxsoft.core.tsgui.ved.tintypes;

public interface ITsResources {

  String STR_N_ENUM = "Перечисление";
  String STR_D_ENUM = "Перечисление";

  String STR_COLOR_COMPONENT   = "";
  String STR_COLOR_COMPONENT_D = "";

  String TSID_COLOR_FIELD_RED     = "Red";
  String TSID_COLOR_FIELD_RED_D   = "Red component of the color (int range 0..255)";
  String TSID_COLOR_FIELD_GREEN   = "Green";
  String TSID_COLOR_FIELD_GREEN_D = "Green component of the color (int range 0..255)";
  String TSID_COLOR_FIELD_BLUE    = "Blue";
  String TSID_COLOR_FIELD_BLUE_D  = "Blue component of the color (int range 0..255)";
  String TSID_COLOR_FIELD_APLHA   = "Alpha";
  String TSID_COLOR_FIELD_APLHA_D = "Alpha component of the color (int range 0..255)";

  String STR_N_COLOR_RGB  = "Цвет";                            // "Цвет"
  String STR_D_COLOR_RGB  = "Цвет заданный компонентами RGB";  // "Цвет заданный компонентами RGB"
  String STR_N_COLOR_RGBA = "Цвет";                            // "Цвет"
  String STR_D_COLOR_RGBA = "Цвет заданный компонентами RGBA"; // "Цвет заданный компонентами RGBA"

  String STR_N_POINT_X = "x";            // "x";
  String STR_D_POINT_X = "x координата"; // "x координата";
  String STR_N_POINT_Y = "y";            // "y";
  String STR_D_POINT_Y = "y координата"; // "y координата";

  String STR_N_D2POINT = "2d-точка";              // "2d-точка";
  String STR_D_D2POINT = "Точка на 2d-плоскости"; // "Точка на 2d-плоскости"

  String STR_N_D2ANGLE = "2d-угол"; // "2d-угол";
  String STR_D_D2ANGLE = "2d-угол"; // "2d-угол";

  String STR_N_D2CONVERSION = "2d-конверсия";                           // "2d-конверсия";
  String STR_D_D2CONVERSION = "Параметры конвертирования 2d-координат"; // "Параметры конвертирования 2d-координат"

  String STR_N_FONT = "Шрифт";            // "Шрифт";
  String STR_D_FONT = "Параметры шрифта"; // "Параметры шрифта";

  String STR_N_FONT_NAME   = "Название";        // "Название";
  String STR_D_FONT_NAME   = "Название шрифта"; // "Название шрифта";
  String STR_N_FONT_SIZE   = "Размер";          // "Размер";
  String STR_D_FONT_SIZE   = "Размер шрифта";   // "Размер шрифта";
  String STR_N_FONT_BOLD   = "Жирный";          // "Жирный";
  String STR_D_FONT_BOLD   = "Жирный шрифт";    // "Жирный шрифт";
  String STR_N_FONT_ITALIC = "Курсив";          // "Курсив";
  String STR_D_FONT_ITALIC = "Стиль курсив";    // "Стиль курсив";

  String STR_N_LINE_INFO  = "Параметры линии";           // Параметры линии
  String STR_D_LINE_INFO  = "Параметры рисования линии"; // Параметры рисования линии
  String STR_N_LINE_THICK = "Толщина";                   // "Толщина";
  String STR_D_LINE_THICK = "Толщина линии";             // "Толщина линии";
  String STR_N_LINE_STYLE = "Стиль";                     // "Стиль";
  String STR_D_LINE_STYLE = "Стиль линии";               // "Стиль линии";
  String STR_N_CAP_STYLE  = "Стиль окончания";           // "Стиль окончания";
  String STR_D_CAP_STYLE  = "Стиль окончания линии";     // "Стиль окончания линии";
  String STR_N_JOIN_STYLE = "Стиль соединения";          // "Стиль соединения";
  String STR_D_JOIN_STYLE = "Стиль соединения линии";    // "Стиль соединения линии";

  String STR_N_BORDER_INFO = "Границы";                        // "Границы";
  String STR_D_BORDER_INFO = "Свойства прямоугольной границы"; // "Свойства прямоугольной границы"

  /**
   * {@link InspFillTypeInfo}
   */
  String STR_N_FILL_TYPE  = "Тип";                  // "Тип";
  String STR_D_FILL_TYPE  = "Тип заливки";          // "Тип заливки";
  String STR_N_FILL_COLOR = "Цвет";                 // "Цвет";
  String STR_D_FILL_COLOR = "Цвет заливки";         // "Цвет заливки";
  String STR_N_IMAGE      = "Изображение";          // "Изображение";
  String STR_D_IMAGE      = "Заливка изображением"; // "Заливка изображением";

  String STR_N_IMAGE_DESCRIPTOR = "Описание";                              // "Описание";
  String STR_D_IMAGE_DESCRIPTOR = "Описание способа создания изображения"; // "Описание способа создания изображения";

  String STR_N_IMAGE_FILL_INFO = "Заливка изображением";           // "Заливка изображением";
  String STR_D_IMAGE_FILL_INFO = "Параметры заливки изображением"; // "Параметры заливки изображением";

  String STR_N_FILL_INFO = "Заливка";           // "Заливка";
  String STR_D_FILL_INFO = "Параметры заливки"; // "Параметры заливки";

  String STR_N_GRADIENT = "Градиент";           // Градиент
  String STR_D_GRADIENT = "Заливка градиентом"; // Заливка градиентом
}
