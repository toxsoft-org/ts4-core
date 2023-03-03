package org.toxsoft.core.tslib.bricks.filter.std.string;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.bricks.filter.std.IStdTsFiltersConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.txtmatch.*;

/**
 * The filter for {@link String} objects.
 * <p>
 * Performs the same filtering as {@link TextMatcher}.
 *
 * @author hazard157
 */
public class StdFilterStringMatcher
    implements ITsFilter<String> {

  /**
   * The filter type ID {@link ITsSingleFilterFactory#id()},
   */
  public static final String TYPE_ID = STD_FILTERID_ID_PREFIX + ".StringMatcher"; //$NON-NLS-1$

  /**
   * Filter factory, an {@link ITsSingleFilterFactory} implementation.
   */
  public static final ITsSingleFilterFactory<String> FACTORY =
      new AbstractTsSingleFilterFactory<>( TYPE_ID, String.class ) {

        @Override
        protected ITsFilter<String> doCreateFilter( IOptionSet aParams ) {
          ETextMatchMode matchMode = aParams.getValobj( PID_MATCH_MODE );
          String constStr = aParams.getStr( PID_CONST_STR );
          boolean isCaseSens = aParams.getBool( PID_CASE_SENS );
          return new StdFilterStringMatcher( matchMode, constStr, isCaseSens );
        }
      };

  private static final String PID_MATCH_MODE = "matchMode";  //$NON-NLS-1$
  private static final String PID_CONST_STR  = "constStr";   //$NON-NLS-1$
  private static final String PID_CASE_SENS  = "isCaseSens"; //$NON-NLS-1$

  private final TextMatcher textMatcher;

  /**
   * Constructor.
   *
   * @param aMatchMode {@link ETextMatchMode} - the string comparison (matching) mode
   * @param aConstString String - the string constant to compare to
   * @param aCaseSensitive boolean - the case sensitivity flag during comparison
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StdFilterStringMatcher( ETextMatchMode aMatchMode, String aConstString, boolean aCaseSensitive ) {
    textMatcher = new TextMatcher( aMatchMode, aConstString, aCaseSensitive );
  }

  /**
   * Makes the parameters {@link ITsCombiFilterParams} to create filter using {@link #FACTORY}.
   *
   * @param aMatchMode {@link ETextMatchMode} - the string comparison (matching) mode
   * @param aConstString String - the string constant to compare to
   * @param aCaseSensitive boolean - the case sensitivity flag during comparison
   * @return {@link ITsCombiFilterParams} - parameters to create the filter
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ITsCombiFilterParams makeFilterParams( ETextMatchMode aMatchMode, String aConstString,
      boolean aCaseSensitive ) {
    TsNullArgumentRtException.checkNulls( aMatchMode, aConstString );
    ITsSingleFilterParams spf = TsSingleFilterParams.create( TYPE_ID, //
        PID_MATCH_MODE, avValobj( aMatchMode ), //
        PID_CONST_STR, avStr( aConstString ), //
        PID_CASE_SENS, avBool( aCaseSensitive ) //
    );
    return TsCombiFilterParams.createSingle( spf );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the text matching mode (the comparison method).
   *
   * @return {@link ETextMatchMode} - the matching mode
   */
  public ETextMatchMode matchMode() {
    return textMatcher.matchMode();
  }

  /**
   * Returns the string constant used to compare accepted string to.
   *
   * @return String - he string constant to compare to
   */
  public String constString() {
    return textMatcher.constString();
  }

  /**
   * Returns a case-sensitive check flag.
   * <p>
   * Some comparison modes (see descriptions of {@link ETextMatchMode} constants) can occur both with and without
   * case-sensitive characters. This feature lets you know how such comparisons occur.
   *
   * @return boolean - the case sensitivity flag during comparison
   */
  public boolean isCaseSensitive() {
    return textMatcher.isCaseSensitive();
  }

  // ------------------------------------------------------------------------------------
  // ITsFilter
  //

  @Override
  public boolean accept( String aString ) {
    return textMatcher.accept( aString );
  }

}
