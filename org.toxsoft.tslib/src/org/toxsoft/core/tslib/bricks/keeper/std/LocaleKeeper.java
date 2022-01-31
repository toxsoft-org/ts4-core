package org.toxsoft.core.tslib.bricks.keeper.std;

import java.util.Locale;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link Locale}.
 * <p>
 * Хранит {@link Locale} в виде только треех компонент {@link Locale#getLanguage()}, {@link Locale#getCountry()} и
 * {@link Locale#getVariant()}.
 *
 * @author goga
 */
public class LocaleKeeper
    extends AbstractEntityKeeper<Locale> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "Locale"; //$NON-NLS-1$

  /**
   * Синглтон хранителя.
   */
  public static final IEntityKeeper<Locale> KEEPER = new LocaleKeeper();

  private LocaleKeeper() {
    super( Locale.class, EEncloseMode.NOT_IN_PARENTHESES, Locale.ROOT );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, Locale aEntity ) {
    aSw.writeQuotedString( aEntity.getLanguage() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.getCountry() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.getVariant() );
  }

  @Override
  protected Locale doRead( IStrioReader aSr ) {
    String language = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String country = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String variant = aSr.readQuotedString();
    return new Locale( language, country, variant );
  }

}
