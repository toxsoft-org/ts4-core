package org.toxsoft.core.tslib.bricks.keeper.std;

import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Хранитель объектов типа {@link Locale}.
 * <p>
 * Хранит {@link Locale} в виде только треех компонент {@link Locale#getLanguage()}, {@link Locale#getCountry()} и
 * {@link Locale#getVariant()}.
 *
 * @author hazard157
 */
public class LocaleKeeper
    extends AbstractEntityKeeper<Locale> {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "Locale"; //$NON-NLS-1$

  /**
   * The keeper singleton.
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
    return Locale.of( language, country, variant );
  }

}
