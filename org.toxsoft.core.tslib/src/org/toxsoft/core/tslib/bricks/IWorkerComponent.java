package org.toxsoft.core.tslib.bricks;

import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * A component that performs its work under the control of a container.
 * <p>
 * Этот интерфейс примешивается (mixin interface) к основному, <b><i>рабочему интерфейсу</i></b> бизнес-логики
 * компоненты. Рабочий интерфейс (или класс) является тем, с чем работает пользователь компоненты, а этот интерфейс
 * всего лишь является особенностью реализации.
 * <p>
 * Любая компонента (модуль, класс), который должен инициализироваться, работать и завершать работу в контексте своего
 * контейнера, может расширять данный интерфейс.
 * <p>
 * Жизненный цикл модуля (компоненты) состоит из:
 * <ul>
 * <li>начало работы - метод {@link #start()};</li>
 * <li>обработка запросов - любые методы рабочего интерфейса, реализованные в наследниках;</li>
 * <li>(опционально) периодическая работа - метод {@link ICooperativeMultiTaskable#doJob()}, вызывается контейнером во
 * время бездействия системы (состояние idle). Этот метод реализует кооперативную многозадачность компонент в
 * контейнере;</li>
 * <li>завершение работы - запрос на завершение {@link #queryStop()}, ожидание завершения {@link #isStopped()},
 * принудительное окончание работы и освобождение всех ресурсов {@link #destroy()}.</li>
 * </ul>
 * Остальные аспекты работы компоненты определяется конкретным типом компоненты контейнера. В частности, наследники
 * определяют поведение компоненты:
 * <ul>
 * <li>разрешен ли повторный запуск компоненты после окончания работы (когда {@link #isStopped()}=true) (после вызова
 * {@link #destroy()} работа компоненты запрещена);</li>
 * <li>разрешено ли создание нескольких экземпляров одного класса компоненты;</li>
 * <li>способ идентификации компонент в контейнере (только ссылки на java-объекты, уникальные идентификаторы и
 * т.п.);</li>
 * <li>может ли компонента запускать свои потоки исполнения. Не рекомендованная практика, предпочитительно осуществить
 * кооперативную (добровольную) многозадачность методом {@link ICooperativeMultiTaskable#doJob()}. При этом, контейнер
 * может создавать свой поток выполнения, и оттуда вызывать {@link ICooperativeMultiTaskable#doJob()} компоненты;</li>
 * <li>а также любые другие аспекты работы контейнера/компоненты.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IWorkerComponent {

  /**
   * Запуск компоненты - после этого компонента начинает работу.
   * <p>
   * Только после выполнения этого метода можно вызывать методы бизнес-логики компоненты, а так же и
   * {@link ICooperativeMultiTaskable#doJob()}, если класс реализует еще и {@link ICooperativeMultiTaskable}.
   * <p>
   * Если компонента уже работает (то есть, {@link #start()} уже был вызван, то повторный вызов ничего не делает.
   *
   * @throws TsRuntimeException (или наследники TsRuntimeException) - по разным причинам, специфичным для компоненты,
   *           невозможно начать работу
   */
  void start();

  /**
   * Запрос остановки работы компоненты.
   * <p>
   * Метод возвращает управление немедленно, даже если работа модуля не может быть завершена. В таком случае, следует
   * опрашивать состояние модуля методом {@link #isStopped()}.
   *
   * @return <b>true</b> - работа компоненты завершена;<br>
   *         <b>false</b> - модуль еще не завершил работу, спрашиваийте у {@link #isStopped()}.
   */
  boolean queryStop();

  /**
   * Определяет, остановлена ли работа компоненты.
   * <p>
   * Контейнер гарантирует, что этот метод вызывается только после {@link #queryStop()}, и только если компонента не
   * остановилсяь. Как только компонента остановилась (то есть, {@link #isStopped()}=true), этот метожд перестает
   * вызываться.
   * <p>
   * Если слишком долго компонента не останавливает работу добровольно, следует насильно вызывать {@link #destroy()} для
   * завершения работы (может быть, не очень корректной) и освобождения ресурсов.
   *
   * @return <b>true</b> - работа компоненты завершена;<br>
   *         <b>false</b> - модуль еще не завершил работу.
   */
  boolean isStopped();

  /**
   * Unconditionally terminates the job and releases all resources.
   * <p>
   * Must be called after the component has finished working, or if the component has been waiting too long for
   * completion, after a timeout. This method must be called before the component reference is given to the garbage
   * collector.
   * <p>
   * Once this method is called, no other method can or should be called and the reference to the component should be
   * garbage collected.
   */
  void destroy();

}
