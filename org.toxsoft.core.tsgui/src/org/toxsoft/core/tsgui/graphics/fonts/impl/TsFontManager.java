package org.toxsoft.core.tsgui.graphics.fonts.impl;

import static org.toxsoft.core.tsgui.graphics.fonts.impl.ITsResources.*;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.core.tsgui.graphics.fonts.IFontInfo;
import org.toxsoft.core.tsgui.graphics.fonts.ITsFontManager;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.impl.ElemMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link ITsFontManager}.
 *
 * @author hazard157
 */
public class TsFontManager
    implements ITsFontManager {

  /**
   * Компаратор для упорядочивания списка доступных шрифтов по имени (без учета регистра).
   */
  private static final Comparator<FontData> FONT_DATA_BY_NAME_COMPARATOR = new Comparator<>() {

    @Override
    public int compare( FontData o1, FontData o2 ) {
      return String.CASE_INSENSITIVE_ORDER.compare( o1.getName(), o2.getName() );
    }
  };

  private final Display display;

  /**
   * Карта "символьное имя" - "шрифт".
   */
  private final IMapEdit<IFontInfo, Font> fontsMap = new ElemMap<>();

  /**
   * Конструктор.
   *
   * @param aDisplay {@link Display} - дисплей
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsFontManager( Display aDisplay ) {
    display = TsNullArgumentRtException.checkNull( aDisplay );
    TsItemNotFoundRtException.checkNull( display );
    // запланируем освобождение ресурсов
    display.disposeExec( new Runnable() {

      @Override
      public void run() {
        releaseResources();
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void releaseResources() {
    while( !fontsMap.isEmpty() ) {
      Font f = fontsMap.removeByKey( fontsMap.keys().first() );
      f.dispose();
    }
  }

  /**
   * Определяет, содержится ли в списке aFontList описание шрифта aFontInfo.
   *
   * @param aFontList IList&lt;{@link IFontInfo}&gt; - просматриваемый список
   * @param aFontInfo {@link IFontInfo} - искомый шрифт
   * @return boolean - <b>true</b> - шрифт со свойствами aFontInfo содержится в aFontList;<br>
   *         <b>false</b> - шрифта aFontInfo нет в aFontList.
   */
  private static boolean isFontInList( IList<IFontInfo> aFontList, IFontInfo aFontInfo ) {
    for( int i = 0, n = aFontList.size(); i < n; i++ ) {
      IFontInfo fi = aFontList.get( i );
      if( fi.fontName().equals( aFontInfo.fontName() ) && fi.isBold() == aFontInfo.isBold()
          && fi.isItalic() == aFontInfo.isItalic() ) {
        return true;
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // ITsFontManager
  //

  @Override
  public IFontInfo data2info( FontData aFontData ) {
    return new FontInfo( aFontData );
  }

  @Override
  public FontData info2data( IFontInfo aFontInfo ) {
    TsNullArgumentRtException.checkNull( aFontInfo );
    int style = 0;
    if( aFontInfo.isBold() ) {
      style |= SWT.BOLD;
    }
    if( aFontInfo.isItalic() ) {
      style |= SWT.ITALIC;
    }
    return new FontData( aFontInfo.fontName(), aFontInfo.fontSize(), style );
  }

  @Override
  public IStringMap<IList<IFontInfo>> loadAvailableFonts() {
    FontData fontDatas[] = display.getFontList( null, true );
    Arrays.sort( fontDatas, FONT_DATA_BY_NAME_COMPARATOR );
    IStringMapEdit<IListEdit<IFontInfo>> fontInfoes = new StringMap<>();
    for( int i = 0; i < fontDatas.length; i++ ) {
      FontData fd = fontDatas[i];
      String fName = fd.getName();
      if( fontInfoes.hasKey( fName ) ) {
        IListEdit<IFontInfo> fList = fontInfoes.getByKey( fName );
        FontInfo fi = new FontInfo( fd );
        if( !isFontInList( fList, fi ) ) {
          // вставим в сортированном порядке: обычный, жирный, курсив, жирный_курсив
          int index = fList.size();
          for( int j = 0; j < fList.size(); j++ ) {
            if( fi.getSwtStyle() < fList.get( j ).getSwtStyle() ) {
              index = j;
              break;
            }
          }
          fList.insert( index, fi );
        }
      }
      else {
        IListEdit<IFontInfo> fList = new ElemArrayList<>( 4 );
        fList.add( new FontInfo( fd ) );
        fontInfoes.put( fName, fList );
      }
    }
    IStringMapEdit<IList<IFontInfo>> result = new StringMap<>();
    for( String s : fontInfoes.keys() ) {
      result.put( s, fontInfoes.getByKey( s ) );
    }
    return result;
  }

  @Override
  public Font getFont( IFontInfo aFontInfo ) {
    TsNullArgumentRtException.checkNull( aFontInfo );
    Font font = fontsMap.findByKey( aFontInfo );
    if( font != null ) {
      return font;
    }
    try {
      font = new Font( display, aFontInfo.fontName(), aFontInfo.fontSize(), aFontInfo.getSwtStyle() );
    }
    catch( Exception ex ) {
      throw new TsIllegalArgumentRtException( ex, FMT_ERR_INV_FONT_CREATION_ARGS, aFontInfo.toString() );
    }
    fontsMap.put( aFontInfo, font );
    return font;
  }

  @Override
  public Font getFont( String aFontName, int aHeight, int aFontStyle ) {
    FontInfo fontInfo = new FontInfo( aFontName, aHeight, aFontStyle );
    return getFont( fontInfo );
  }

}
