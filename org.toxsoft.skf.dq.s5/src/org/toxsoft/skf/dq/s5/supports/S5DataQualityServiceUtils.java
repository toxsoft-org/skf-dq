package org.toxsoft.skf.dq.s5.supports;

import static org.toxsoft.skf.dq.s5.supports.IS5Resources.*;

import org.toxsoft.core.tslib.bricks.threadexec.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.dq.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.gwids.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Методы поддержки службы {@link ISkDataQualityService}
 *
 * @author mvk
 */
public class S5DataQualityServiceUtils {

  /**
   * Вспомогательный класс для запросов {@link GwidList}
   *
   * @author mvk
   */
  private static class ExpandQuery
      implements Runnable {

    private final ISkCoreApi coreApi;
    private final Gwid       gwid;
    private final IGwidList  gwids;
    private GwidList         retValue = new GwidList();

    ExpandQuery( ISkCoreApi aCoreApi, Gwid aGwid ) {
      coreApi = TsNullArgumentRtException.checkNull( aCoreApi );
      gwids = null;
      gwid = aGwid;
    }

    ExpandQuery( ISkCoreApi aCoreApi, IGwidList aGwids ) {
      coreApi = TsNullArgumentRtException.checkNull( aCoreApi );
      gwids = TsNullArgumentRtException.checkNull( aGwids );
      gwid = null;
    }

    @Override
    public void run() {
      ISkGwidService gwidService = coreApi.gwidService();
      if( gwid != null ) {
        retValue = gwidService.expandGwid( gwid );
        return;
      }
      retValue = new GwidList();
      for( Gwid g : gwids ) {
        IGwidList expandGwids = gwidService.expandGwid( g );
        for( Gwid expandGwid : expandGwids ) {
          if( !retValue.hasElem( g ) ) {
            retValue.add( expandGwid );
          }
        }
      }
    }

    IGwidList retValue() {
      return retValue;
    }
  }

  /**
   * Проводит разгруппировку указанного идентификатора.
   * <p>
   * Под разгруппировкой {@link Gwid} понимается замена групповых ({@link Gwid#isMulti()} == true), на НЕгрупповые
   * ({@link Gwid#isMulti()} == false). Таким образом в возращаемом списке {@link Gwid} представлены {@link Gwid}
   * КОНКРЕТНОГО данного/события/команды/... КОНКРЕТНОГО объекта.
   * <ul>
   * <li>Если {@link Gwid} представляет много объектов (*), то производится создание {@link Gwid} для каждого
   * объекта.</li>
   * <li>Если {@link Gwid} представляет много данных/событий/команд/...(*), то производится создание {@link Gwid}
   * соотвествующего типа для каждого данного/события/команды/....</li>
   * </ul>
   *
   * @param aCoreApi {@link ISkCoreApi} API локального соединения с сервером
   * @param aGwid {@link Gwid} идентификатор ресурсов
   * @return {@link IGwidList} список {@link Gwid} полученных после нормализации
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException запрет абстрактных {@link Gwid} - должен быть указан объект или объекты(*)
   * @throws TsIllegalArgumentRtException {@link Gwid} не представляют данное {@link EGwidKind#GW_RTDATA}
   * @throws TsIllegalArgumentRtException класс, объект или данное не существует в системе при aCheckExist = true
   */
  public static IGwidList ungroupGwid( ISkCoreApi aCoreApi, Gwid aGwid ) {
    TsNullArgumentRtException.checkNulls( aCoreApi, aGwid );
    ITsThreadExecutor threadExecutor = SkThreadExecutorService.getExecutor( aCoreApi );
    ExpandQuery query = new ExpandQuery( aCoreApi, aGwid );
    threadExecutor.syncExec( query );
    return query.retValue();
  }

  /**
   * Проводит разгруппировку указанного идентификатора.
   * <p>
   * Под разгруппировкой {@link Gwid} понимается замена групповых ({@link Gwid#isMulti()} == true), на НЕгрупповые
   * ({@link Gwid#isMulti()} == false). Таким образом в возращаемом списке {@link Gwid} представлены {@link Gwid}
   * КОНКРЕТНОГО данного/события/команды/... КОНКРЕТНОГО объекта.
   * <ul>
   * <li>Если {@link Gwid} представляет много объектов (*), то производится создание {@link Gwid} для каждого
   * объекта.</li>
   * <li>Если {@link Gwid} представляет много данных/событий/команд/...(*), то производится создание {@link Gwid}
   * соотвествующего типа для каждого данного/события/команды/....</li>
   * </ul>
   *
   * @param aCoreApi {@link ISkCoreApi} API локального соединения с сервером
   * @param aGwids {@link IGwidList} список идентификаторов ресурсов
   * @return {@link IGwidList} список {@link Gwid} полученных после нормализации
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException запрет абстрактных {@link Gwid} - должен быть указан объект или объекты(*)
   * @throws TsIllegalArgumentRtException {@link Gwid} не представляют данное {@link EGwidKind#GW_RTDATA}
   * @throws TsIllegalArgumentRtException класс, объект или данное не существует в системе при aCheckExist = true
   */
  public static IGwidList ungroupGwids( ISkCoreApi aCoreApi, IGwidList aGwids ) {
    TsNullArgumentRtException.checkNulls( aCoreApi, aGwids );
    ITsThreadExecutor threadExecutor = SkThreadExecutorService.getExecutor( aCoreApi );
    ExpandQuery query = new ExpandQuery( aCoreApi, aGwids );
    threadExecutor.syncExec( query );
    return query.retValue();
  }

  /**
   * Проверяет {@link Gwid} на предмет того, что он представляет данное объекта (смотри {@link EGwidKind#GW_RTDATA})
   * <p>
   * {@link Gwid} ресурсов должны представлять данные объектов. Абстрактные {@link Gwid} (без объекта(ов)) не
   * допускаются. Примеры возможных {@link Gwid}:
   * <ul>
   * <li>CtPot[potObj1]$rtdata( alive ).</li>
   * <li>CtPot[potObj1]$rtdata( * ).</li>
   * <li>CtPot[*]$rtdata( alive ).</li>
   * <li>CtPot[*]$rtdata( * ).</li>
   * </ul>
   *
   * @param aGwid {@link Gwid} проверяемый идентификатор {@link Gwid}
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException запрет абстрактных {@link Gwid} - должен быть указан объект или объекты(*)
   * @throws TsIllegalArgumentRtException {@link Gwid} не представляют данное {@link EGwidKind#GW_RTDATA}
   */
  public static void checkDataGwid( Gwid aGwid ) {
    TsNullArgumentRtException.checkNull( aGwid );
    if( aGwid.isAbstract() ) {
      // Запрет использования абстрактных Gwid
      throw new TsIllegalArgumentRtException( ERR_ABSTRACT_GWID, aGwid );
    }
    if( aGwid.kind() != EGwidKind.GW_RTDATA ) {
      // ugwi должен представлять данное объекта
      throw new TsIllegalArgumentRtException( ERR_NO_DATA_GWID, aGwid );
    }
  }

  /**
   * Возвращает список {@link Gwid} из списка текстовых представлений {@link Gwid}
   *
   * @param aResources {@link IStringList} список текстовых представлений {@link Gwid}
   * @return {@link GwidList} список {@link Gwid} с возможностью редактирования
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException запрет абстрактных {@link Gwid} - должен быть указан объект или объекты(*)
   * @throws TsIllegalArgumentRtException {@link Gwid} не представляют данное {@link EGwidKind#GW_RTDATA}
   */
  public static GwidList stringsToUgwies( IStringList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    GwidList retValue = new GwidList();
    for( String resource : aResources ) {
      // Удаление пробелов и создание IUgwi
      Gwid ugwi = Gwid.KEEPER.str2ent( resource );
      // Проверка ugwi
      checkDataGwid( ugwi );
      // Сохранение в списке
      retValue.add( ugwi );
    }
    return retValue;
  }

  /**
   * Возвращает текстовый список из списка {@link Gwid}
   *
   * @param aResources {@link IGwidList} список {@link Gwid}
   * @return {@link IStringListEdit} текстовый список с возможностью редактирования
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException запрет абстрактных {@link Gwid} - должен быть указан объект или объекты(*)
   * @throws TsIllegalArgumentRtException {@link Gwid} не представляют данное {@link EGwidKind#GW_RTDATA}
   */
  public static IStringListEdit ugwiesToStrings( IGwidList aResources ) {
    TsNullArgumentRtException.checkNull( aResources );
    IStringListEdit retValue = new StringArrayList( aResources.size() );
    for( Gwid gwid : aResources ) {
      // Проверка ugwi
      checkDataGwid( gwid );
      // Сохранение в списке с удалением пробелов
      retValue.add( Gwid.KEEPER.ent2str( gwid ) );
    }
    return retValue;
  }

  // /**
  // * ТЕСТЫ
  // *
  // * @param args
  // */
  // @SuppressWarnings( "nls" )
  // public static void main( String[] args ) {
  // checkUgwi( new Ugwi( "CtPot[*]:data( * )" ) );
  // checkUgwi( new Ugwi( new String( "s5.class.User[*]:data( * )" ).replace( " ", "" ) ) );
  // checkUgwi( new Ugwi( "CtPot[ -56879 ]:data( * )" ) );
  // checkUgwi( new Ugwi( "CtPot[ 123 ]:data( alive )" ) );
  // checkUgwi( new Ugwi( "CtPot[ potObj1 ]:data( * )" ) );
  // checkUgwi( new Ugwi( "CtPot[ potObj1 ]:data( alive )" ) );
  // }
  //
  // @SuppressWarnings( "nls" )
  // private static void checkUgwi( Ugwi ugwi ) {
  // System.out.println( ugwi + //
  // ", normal = " + ugwi.asString().replaceAll( " ", "" ) + //
  // ", type = " + ugwi.type() + //
  // ", classId = " + ugwi.classId() + //
  // ", objId = " + ugwi.objId() + //
  // ", objStrid = '" + ugwi.objStrid() + //
  // "', dataId = '" + ugwi.dataId() + //
  // "', propId = '" + ugwi.propId() + //
  // "', subPropId() = " + ugwi.subPropId() + //
  // ", isAbstract() = " + ugwi.isAbstract() + //
  // ", isMulti() = " + ugwi.isMulti() + //
  // ", isAllIds() = " + ugwi.isAllIds() + //
  // ", isAllObjs() = " + ugwi.isAllObjs() + //
  // ", isObjId() = " + ugwi.isObjId() );
  // }

}
