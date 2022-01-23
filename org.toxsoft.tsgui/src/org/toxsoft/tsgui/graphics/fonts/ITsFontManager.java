package org.toxsoft.tsgui.graphics.fonts;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.primtypes.IStringMap;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Менеджер работы со шрифтами {@link Font}.
 * <p>
 * Ссылка на экземпляр этого класса должен находится в контексте приложения.
 *
 * @author hazard157
 */
public interface ITsFontManager {

  /**
   * Возвращает описание шрифта, созданный на основе {@link FontData}.
   *
   * @param aFontData {@link FontData} - SWT-данные о шрифте
   * @return {@link IFontInfo} - созданное описание шрифта
   * @throws TsNullArgumentRtException аргумент = null
   */
  IFontInfo data2info( FontData aFontData );

  /**
   * Возвращает SWT-данные о шрифте {@link FontData}, созданный на основе {@link IFontInfo}.
   *
   * @param aFontInfo {@link IFontInfo} - описание шрифта
   * @return {@link FontData} - SWT-данные о шрифте созданный по aFontInfo
   * @throws TsNullArgumentRtException аргумент = null
   */
  FontData info2data( IFontInfo aFontInfo );

  // ------------------------------------------------------------------------------------
  // Запрос шрифтов в системе
  //

  /**
   * Возвращает описания всех доступных в системе масштабируемых шрифтов.
   * <p>
   * Метод возвращает карту всех масштабируемых шрифтов, доступных в системе. В качестве ключа в карте служит имя
   * шрифта. При этом, список ключей {@link IStringMap#keys()} отсортировано по именам без учета регистра. В качестве
   * значений, в карте содержится список IList&lt;{@link IFontInfo}&gt; описаний доступных стилей шрифта. В списке по
   * имени шрифта может до четырех описаний шрифтов (по количеству сочетаний значений признаков <b>жирный</b> и
   * <i>курсив</i>. В карте не содержатся ни повторяющейся имена, ни повторяющейся описания шрифтов.
   * <p>
   * Обратите внимание, что поскольку запрашиваются только масштабируемые шрифты, то значение размера шрифта (поле
   * {@link IFontInfo#fontSize()}) равно 0 у всех возвращаемых описаний.
   *
   * @return IStringMap&lt;IList&lt;IFontInfo&gt;&gt; - карта "имя шрифта" - "список вариантов шрифта"
   */
  IStringMap<IList<IFontInfo>> loadAvailableFonts();

  // ------------------------------------------------------------------------------------
  // Кешированное получение шрифтов
  //

  /**
   * Создает новый шрифт на основе заданных параметров или возвращает существующий.
   *
   * @param aFontInfo {@link IFontInfo} - описание (параметры) шрифта
   * @return {@link Font} - созданный (кешированный) шрифт или null для аргумента {@link IFontInfo#NULL}
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException система не может создать шрифт с такими свойствами
   */
  Font getFont( IFontInfo aFontInfo );

  /**
   * Создает новый шрифт на основе заданных параметров или возвращает существующий.
   *
   * @param aFontName String - имя шрифта
   * @param aHeight int - высота шрифта в пунктах
   * @param aFontStyle int - стиль, комбинация констант SWT.BOLD, SWT.ITALIC, SWT.UNDERLINE_XXX
   * @return {@link Font} - созданный (кешированный) шрифт
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException aFonName - пустая строка
   * @throws TsIllegalArgumentRtException система не может создать шрифт с такими свойствами
   */
  Font getFont( String aFontName, int aHeight, int aFontStyle );

}
