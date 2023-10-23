package org.toxsoft.core.tsgui.graphics.fonts.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link IFontInfo} immutable implementation.
 *
 * @author hazard157
 */
public class FontInfo
    implements IFontInfo {

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "FontInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<IFontInfo> KEEPER =
      new AbstractEntityKeeper<>( IFontInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, IFontInfo.NULL ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IFontInfo aEntity ) {
          aSw.writeQuotedString( aEntity.fontName() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.fontSize() );
          aSw.writeSeparatorChar();
          aSw.writeBoolean( aEntity.isBold() );
          aSw.writeSeparatorChar();
          aSw.writeBoolean( aEntity.isItalic() );
        }

        @Override
        protected IFontInfo doRead( IStrioReader aSr ) {
          String fontNname = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          int size = aSr.readInt();
          aSr.ensureSeparatorChar();
          boolean isBold = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          boolean isItalic = aSr.readBoolean();
          return new FontInfo( fontNname, size, isBold, isItalic );
        }
      };

  /**
   * Default keeped atomic value of {@link IFontInfo#NULL}.
   */
  public static final IAtomicValue AV_FONT_INFO_NULL = AvUtils.avValobj( IFontInfo.NULL, KEEPER, KEEPER_ID );

  private final String  fontName;
  private final int     fontSize;
  private final boolean bold;
  private final boolean italic;
  private final int     hashCode;

  /**
   * Constructor.
   *
   * @param aFontName String - font typeface name
   * @param aSize int - font size in points (1/72 inch)
   * @param aBold boolean - <code>true</code> bold font
   * @param aItalic boolean - <code>true</code> italic font
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException font name is a blank string
   * @throws TsIllegalArgumentRtException aSize < 0
   */
  public FontInfo( String aFontName, int aSize, boolean aBold, boolean aItalic ) {
    fontName = TsErrorUtils.checkNonBlank( aFontName );
    TsIllegalArgumentRtException.checkTrue( aSize < 0 );
    fontSize = aSize;
    bold = aBold;
    italic = aItalic;
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + fontName.hashCode();
    result = TsLibUtils.PRIME * result + fontSize;
    result = TsLibUtils.PRIME * result + (bold ? 1 : 0);
    result = TsLibUtils.PRIME * result + (italic ? 1 : 0);
    hashCode = result;
  }

  /**
   * Constructor.
   *
   * @param aFontName String - font typeface name
   * @param aSize int - font size in points (1/72 inch)
   * @param aFontStyleSwtBits int - ORed SWT bits {@link SWT#BOLD}, {@link SWT#ITALIC}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException font name is a blank string
   * @throws TsIllegalArgumentRtException aSize < 0
   */
  public FontInfo( String aFontName, int aSize, int aFontStyleSwtBits ) {
    this( aFontName, aSize, (aFontStyleSwtBits & SWT.BOLD) != 0, (aFontStyleSwtBits & SWT.ITALIC) != 0 );
  }

  /**
   * Constructor.
   *
   * @param aFontData {@link FontData} - SWT font data
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FontInfo( FontData aFontData ) {
    this( TsNullArgumentRtException.checkNull( aFontData ).getName(), aFontData.getHeight(),
        (aFontData.getStyle() & SWT.BOLD) != 0, (aFontData.getStyle() & SWT.ITALIC) != 0 );
  }

  /**
   * Constructor based on sample.
   *
   * @param aFontInfo {@link IFontInfo} - the sample font info
   * @param aZoomFactor float - the font size zoom factor (1.0 = original size)
   * @param aNewStyle int - ORed SWT bits {@link SWT#BOLD}, {@link SWT#ITALIC}
   * @return {@link Font} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aZoomFactor <= 0.0
   */
  public static IFontInfo createAdjusted( IFontInfo aFontInfo, float aZoomFactor, int aNewStyle ) {
    TsNullArgumentRtException.checkNull( aFontInfo );
    TsIllegalArgumentRtException.checkTrue( aZoomFactor <= 0.0 );
    String fontName = aFontInfo.fontName();
    int fontSize = (int)(aZoomFactor * ((double)aFontInfo.fontSize()));
    boolean bold = (aNewStyle & SWT.BOLD) != 0;
    boolean italic = (aNewStyle & SWT.ITALIC) != 0;
    return new FontInfo( fontName, fontSize, bold, italic );
  }

  /**
   * Constructor based on source.
   *
   * @param aFontInfo {@link IFontInfo} - образец шрифта
   * @param aZoomFactor float - the font size zoom factor (1.0 = original size)
   * @param aBold boolean - <code>true</code> bold font
   * @param aItalic boolean - <code>true</code> italic font
   * @return {@link Font} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aZoomFactor <= 0.0
   */
  public static IFontInfo createAdjusted( IFontInfo aFontInfo, float aZoomFactor, boolean aBold, boolean aItalic ) {
    TsNullArgumentRtException.checkNull( aFontInfo );
    TsIllegalArgumentRtException.checkTrue( aZoomFactor <= 0.0 );
    String fontName = aFontInfo.fontName();
    int fontSize = (int)(aZoomFactor * ((double)aFontInfo.fontSize()));
    boolean bold = aBold;
    boolean italic = aItalic;
    return new FontInfo( fontName, fontSize, bold, italic );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append( '(' );
    sb.append( '"' );
    sb.append( fontName );
    sb.append( '"' );
    sb.append( ',' );
    sb.append( fontSize );
    if( bold ) {
      sb.append( ',' );
      sb.append( "BOLD" ); //$NON-NLS-1$
    }
    if( bold ) {
      sb.append( ',' );
      sb.append( "ITALIC" ); //$NON-NLS-1$
    }
    sb.append( ')' );
    return sb.toString();
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj instanceof IFontInfo that ) {
      return hashCode == that.hashCode() && that.fontName().equals( fontName ) && (that.fontSize() == fontSize)
          && (that.isBold() == bold) && (that.isItalic() == italic);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  // --------------------------------------------------------------------------
  // IFontInfo
  //

  @Override
  public String fontName() {
    return fontName;
  }

  @Override
  public int fontSize() {
    return fontSize;
  }

  @Override
  public boolean isBold() {
    return bold;
  }

  @Override
  public boolean isItalic() {
    return italic;
  }

  @Override
  public int getSwtStyle() {
    int style = 0;
    if( isBold() ) {
      style |= SWT.BOLD;
    }
    if( isItalic() ) {
      style |= SWT.ITALIC;
    }
    return style;
  }

}
