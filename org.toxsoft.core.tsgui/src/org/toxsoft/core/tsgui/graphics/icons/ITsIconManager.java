package org.toxsoft.core.tsgui.graphics.icons;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The icon manager.
 *
 * @author hazard157
 */
public interface ITsIconManager {

  /**
   * Icon files path in the plugin.
   */
  String PATH_TO_ICONS = "icons/"; //$NON-NLS-1$

  /**
   * Icon file name extention.
   */
  String ICON_FILE_EXTENSION = ".png"; //$NON-NLS-1$

  /**
   * Создает символьное имя значка из идентификатора и указания размера.
   *
   * @param aStdIconId String - идентификатор стандартного значка
   * @param aSize {@link EIconSize} - размер значка
   * @return String - символьное имя значка
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор значка пустая строка
   */
  String makeSymbolicName( String aStdIconId, EIconSize aSize );

  /**
   * Загружает ранее зарегистрированный стандартный значок.
   * <p>
   * Если нет изображения для значка (например, идентификатор не был зарегистрирован), возвращает изображение значка
   * {@link ITsStdIconIds#ICONID_UNKNOWN_ICON_ID} соответствующего размера.
   *
   * @param aStdIconId String - идентификатор значка
   * @param aSize {@link EIconSize} - размер значка
   * @return {@link Image} - изображение значка указанного размера
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  Image loadStdIcon( String aStdIconId, EIconSize aSize );

  /**
   * Загружвает ранее зарегистрированный произвольный значок.
   * <p>
   * Если нет изображения для значка (например, идентификатор не был зарегистрирован), возвращает изображение значка
   * {@link ITsStdIconIds#ICONID_UNKNOWN_ICON_ID} размером {@link EIconSize#maxSize()}.
   *
   * @param aSymbolicName String - зарегистрированное символьное имя значка
   * @return {@link Image} - загруженное изображение или изображение неизвестного значка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  Image loadFreeIcon( String aSymbolicName );

  /**
   * Загружает ранее зарегистрированный стандартный значок в виде дескриптора.
   * <p>
   * If no such icon was registered returns descriptor of {@link ITsStdIconIds#ICONID_UNKNOWN_ICON_ID}.
   *
   * @param aStdIconId String - идентификатор значка
   * @param aSize {@link EIconSize} - размер значка
   * @return {@link ImageDescriptor} - дескриптор значка указанного размера
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ImageDescriptor loadStdDescriptor( String aStdIconId, EIconSize aSize );

  /**
   * Загружвает ранее зарегистрированный произвольный значок в виде дескриптора.
   * <p>
   * Если нет изображения для значка (например, идентификатор не был зарегистрирован), возвращает изображение значка
   * {@link ITsStdIconIds#ICONID_UNKNOWN_ICON_ID} размером {@link EIconSize#maxSize()}.
   *
   * @param aSymbolicName String - зарегистрированное символьное имя значка
   * @return {@link ImageDescriptor} - дескриптор значка
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException такой значок не был зарегистрирован
   */
  ImageDescriptor loadFreeDescriptor( String aSymbolicName );

  /**
   * Возвращает (по возможности) URI для доступа к значку, как русурсу в плагине.
   * <p>
   * Если значок не зарегистрирован, или не является ресурсом в плагине, возвращает <code>null</code>.
   * <p>
   * Возвращаемое значение можно использовать, чтобы задать значки сущностям Eclipse E4, например, методом
   * <code>MPart.setIconUri()</code>.
   *
   * @param aStdIconId String - идентификатор значка
   * @param aSize {@link EIconSize} - размер значка
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @return String - строка URI ресурса в пагине или <code>null</code>
   */
  String findStdIconBundleUri( String aStdIconId, EIconSize aSize );

  /**
   * Определяет, зарегистрирован ли значко с указанным символическим именем.
   *
   * @param aSymbolicName String - зарегистрированное символьное имя значка
   * @return boolean - признак, что такой значок зарегистрирован
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  boolean isRegistered( String aSymbolicName );

  /**
   * Регистрирует ресурс стандартного значка.
   * <p>
   * Если идентификатор уже использовался, то изображение значка будет переопределено, в том числе и для значков из
   * {@link ITsStdIconIds}.
   *
   * @param aPluginId String - идентификатор плагина
   * @param aStdIconId String - идентификатор значка
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException любой аргумент - пустая строка
   */
  void registerStdIcon( String aPluginId, String aStdIconId );

  /**
   * Регистрирует ресурс произвольного значка под указанным символическим именем.
   * <p>
   * Если символическое имя уже использовалось, то изображение значка будет переопределено.
   *
   * @param aPluginId String - идентификатор eclipse-плагина, в котором находится ресурс
   * @param aPathInPlugin String - путь к ресурсу в плагине (например, "icons/UserFace.jpg")
   * @param aSymbolicName String - символическое имя (например, "myapp_login_user_face")
   * @return boolean - признак успешной регистрации<br>
   *         <b>true</b> - ресурс значка найден и зарегистрирован;<br>
   *         <b>false</b> - нет такого ресурса в указанном плагине.
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException любой аргумент - пустая строка
   */
  boolean registerFreeIcon( String aPluginId, String aPathInPlugin, String aSymbolicName );

  /**
   * Регистрирует стандартные значки, перечисленные в указанном интерфейсе (классе).
   * <p>
   * Этот метод позволяет в каком либо интерфейсе приложения сделать перечень констант приложение-специфичных значков,
   * используемых программой. Вызов этого метода приведет к регистрации всех значков для последующего доступа к ним
   * методом {@link #loadStdIcon(String, EIconSize)}.
   * <p>
   * Внимание: напомним, что значением констант являются идентификаторы значка, то есть путь значка относительно папки
   * "icons/isWWxXX" без расширения.
   * <p>
   * <h3>Пример</h3>
   * <p>
   * В плагине "my_plugin" раполагаются значки:
   * <ul>
   * <li>icons/is16x16/sign.png</li>
   * <li>icons/is16x16/myapp/goodicon.png</li>
   * <li>icons/is16x16/s5/logo.png</li>
   * <li>icons/is24x24/sign.png</li>
   * <li>icons/is24x24/myapp/goodicon.png</li>
   * <li>icons/is24x24/s5/logo.png</li>
   * <li>icons/is32x32/sign.png</li>
   * <li>icons/is32x32/myapp/goodicon.png</li>
   * <li>icons/is32x32/s5/logo.png</li>
   * </ul>
   * <p>
   * Соответственно, создается Java-интерфейс:
   * <p>
   * <code>
   * interface IGuiConsts {<br>
   * &nbsp;&nbsp;String AUTOLOAD_ICONS_PREFIX = &quot;MYICON_&quot;; // префикс имен Java-констант<br>
   * &nbsp;&nbsp;String MYICON_PERS_SIGN = &quot;some&quot;;<br>
   * &nbsp;&nbsp;String MYICON_PERS_S5_LOGO = &quot;s5/logo&quot;;<br>
   * &nbsp;&nbsp;String MYICON_PERS_MYAPP_GOODICON = &quot;myapp/goodicon&quot;;<br>
   * }<br>
   * Обратите внимание, что в имени значка нет расширения, но есть указание на поддиректории относительно icons/isXXxHH/.
   * <p>
   * </code> Для этого кода следует сделать следующий вызов:<br>
   * <code>{@link #registerStdIconByIds(String, Class, String)
   * registerStdIconByIds("my_plugin",IGuiConsts.class,IGuiConsts.AUTOLOAD_ICONS_PREFIX)};</code>
   * <p>
   * Учтите, для отсутствующих размеров (например, 24, 48, 64 пикселей в примере) буздут загружены изобрежаения
   * неизвестного значка {@link ITsStdIconIds#ICONID_UNKNOWN_ICON_ID}.
   *
   * @param aPluginId String - идентификатор плагина
   * @param aInterfaceClass {@link Class} - класс, содержащий контсанты имен изображений
   * @param aIconNamePrefix String - префикс имен Java-констант регистрируемых значков
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор плагина пустая строка
   * @throws TsIllegalStateRtException нарушение соглашений этого метода при обявлений полей интерфейса (класса)
   */
  void registerStdIconByIds( String aPluginId, Class<?> aInterfaceClass, String aIconNamePrefix );

}
