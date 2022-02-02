package org.toxsoft.core.tslib.utils.files;

/**
 * File copy progress monitoring callback.
 *
 * @author hazard157
 */
public interface IFileOperationProgressCallback {

  // FIXME этот интерфейс должен быть заменен на общий интерфейс типа ILongOperationCallback

  /**
   * "null" callback, does nothing.
   */
  IFileOperationProgressCallback NULL = new IFileOperationProgressCallback() {

    @Override
    public boolean onFileCopyProgress( long aTotalSteps, long aCurrentStep ) {
      return false;
    }
  };

  // TRANSLATE

  /**
   * Вызывается при очередном шаге копирования файла(ов).
   * <p>
   * Этот метод вызвается aTotalSteps<b>+1</b> раз. В первый раз, со значением aCurrentStep<b>=0</b> метод вызыватеся до
   * начала копирования. В дальнейшем, метод вызвается после копирования очередно части файла с увеличивающимся
   * значением.
   *
   * @param aTotalSteps long - общее количество шагов к выполнению (не меняется со временем)
   * @param aCurrentStep long - номер выполненного шага
   * @return boolean - признак прекращения процесса копирования<br>
   *         <b>true</b> - копирование немедленно прекращается, оставив копируемый файл в полузаписанном виде;<br>
   *         <b>false</b> - копирование продолжается.
   */
  boolean onFileCopyProgress( long aTotalSteps, long aCurrentStep );

}
