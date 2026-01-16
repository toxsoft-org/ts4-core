package org.toxsoft.core.tsgui.valed.controls.enums;

import static org.toxsoft.core.tsgui.valed.controls.enums.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.controls.enums.IValedEnumConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Базовый класс для облегчения реализации редакторов enum-перечисляемых значений.
 * <p>
 * При создании редактора следует задать класс используемого перечисления {@link #enumClass()} одним из следующих
 * способов (в порядке убивания приоритета):
 * <ul>
 * <li>указывается в одном из конструкторов {@link #AbstractValedEnum(ITsGuiContext, Class, ITsVisualsProvider)}.</li>
 * <li>в контексте {@link ITsGuiContext} перед вызовом конструктора {@link #AbstractValedEnum(ITsGuiContext)} (или
 * фабричного метода) заносится ссылка {@link IValedEnumConstants#REFDEF_ENUM_CLASS};</li>
 * <li>в параметры контекста {@link ITsGuiContext#params()} перед вызовом конструктора
 * {@link #AbstractValedEnum(ITsGuiContext)} (или фабричного метода) заносится имя класса enum-а в параметр
 * {@link IValedEnumConstants#OPDEF_ENUM_CLASS_NAME}.</li>
 * </ul>
 * Если ни одни из способв не задал класс перечисления, то метод {@link #createControl(Composite)} выбросит исключение
 * {@link TsIllegalStateRtException}.
 * <p>
 * Обратите внимание, что используемео перечисление должно содержать хотя бы одну константу, иначе конструкторы выбросят
 * исключение {@link TsIllegalArgumentRtException}.
 *
 * @author hazard157
 * @param <V> - the edited enum type
 * @param <C> - the underlying widget type
 */
public abstract class AbstractValedEnum<V extends Enum<V>, C extends Control>
    extends AbstractValedControl<V, C> {

  private final IListEdit<V> items = new ElemArrayList<>();

  private Class<V> enumClass;

  /**
   * Constructor with mandatory arguments.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @param aEnumClass {@link Class} - the <code>enum</code> class
   * @param aVisualsProvider {@link ITsVisualsProvider} - the constants visual representation provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public AbstractValedEnum( ITsGuiContext aTsContext, Class<V> aEnumClass, ITsVisualsProvider<V> aVisualsProvider ) {
    super( aTsContext );
    TsNullArgumentRtException.checkNulls( aEnumClass, aVisualsProvider );
    enumClass = aEnumClass;
    setVisualsProvider( aVisualsProvider );
    internalUpdateItems();
  }

  /**
   * Constructs instance with information from context.
   * <p>
   * The context must contain either {@link IValedEnumConstants#REFDEF_ENUM_CLASS} reference or
   * {@link IValedEnumConstants#OPDEF_ENUM_CLASS_NAME}. Optionally
   * {@link IValedControlConstants#REFDEF_VALUE_VISUALS_PROVIDER} is recoginzed and used if present.
   *
   * @param aTsContext {@link ITsGuiContext} - th editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException context does not contains mandatory information
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public AbstractValedEnum( ITsGuiContext aTsContext ) {
    super( aTsContext );
    enumClass = getEnumClassFromContext( aTsContext );
    internalUpdateItems();
  }

  private final void internalUpdateItems() {
    items.setAll( enumClass().getEnumConstants() );
    if( items.isEmpty() ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_EMPTY_ENUM, getClass().getSimpleName(),
          enumClass.getSimpleName() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методоы базового класса
  //

  @Override
  final protected C doCreateControl( Composite aParent ) {
    TsIllegalStateRtException.checkNull( enumClass, MSG_ERR_ENUM_CLASS_NOT_SPECIFIED, this.getClass().getSimpleName() );
    return doDoCreateControl( aParent );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает класс перечисления, используемый как источник выбираемых элементов.
   *
   * @return {@link Class}&lt;V&gt; - класс перечисления или <code>null</code> до инициализации
   */
  final public Class<V> enumClass() {
    return enumClass;
  }

  /**
   * Возвращает список констант перечисения.
   * <p>
   * Гарантируется, что в списке есть хотя бы один элемент. То есть, если enum-перечисление пустое, еще в конструкторе
   * будет выброшено исключение.
   *
   * @return IList&lt;V&gt; - список констант перечисения в порядке их обявления
   */
  public final IList<V> items() {
    return items;
  }

  // ------------------------------------------------------------------------------------
  // Методы, обязательные для реализации наследниками
  //

  /**
   * Реализация должна создать панель, реализующий этот редактор.
   * <p>
   * Метод вызывается из {@link #doCreateControl(Composite)}.
   *
   * @param aParent {@link Composite} - родительская панель
   * @return &lt;C&gt; - созданный контроль
   */
  abstract protected C doDoCreateControl( Composite aParent );

}
