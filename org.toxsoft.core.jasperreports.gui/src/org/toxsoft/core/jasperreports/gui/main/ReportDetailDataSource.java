package org.toxsoft.core.jasperreports.gui.main;

import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import net.sf.jasperreports.engine.*;

/**
 * Поставщик данных jasperreport для области detail.<br>
 *
 * @author mvk
 */
public class ReportDetailDataSource
    implements JRDataSource {

  // Итератор по результатам
  private Iterator<Map<String, IAtomicValue>> iterator;

  // Текущая карта в списке данных
  private Map<String, IAtomicValue> currentMap;

  /**
   * Создает набор данных на основе модели данных
   * <p>
   * Ключом карты элемента являет имя параметра, значение его строковое значение
   *
   * @param aMapList список моделей данных для отчета
   */
  public ReportDetailDataSource( List<Map<String, IAtomicValue>> aMapList ) {
    this.iterator = aMapList.iterator();
  }

  /*
   * (non-Javadoc)
   * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
   */
  @Override
  public Object getFieldValue( JRField aField )
      throws JRException {
    /**
     * Список полей 1. time_field - метка времени 2. column_1 - column_15 - поля результатов параметров
     */
    // Очень сложный алгоритм поиска данных ;-)
    IAtomicValue retValue = currentMap.get( aField.getName() );
    if( retValue == null ) {
      return TsLibUtils.EMPTY_STRING;
    }
    switch( retValue.atomicType() ) {
      case NONE:
        return TsLibUtils.EMPTY_STRING;
      case BOOLEAN:
        return Boolean.valueOf( retValue.asBool() );
      case INTEGER:
        return Integer.valueOf( retValue.asInt() );
      case FLOATING:
        return Double.valueOf( retValue.asDouble() );
      case TIMESTAMP:
        return new Date( retValue.asLong() );
      case STRING:
        return retValue.asString();
      case VALOBJ:
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    return retValue;
  }

  @Override
  public boolean next()
      throws JRException {
    currentMap = iterator.hasNext() ? iterator.next() : null;
    return (currentMap != null);
  }
}
