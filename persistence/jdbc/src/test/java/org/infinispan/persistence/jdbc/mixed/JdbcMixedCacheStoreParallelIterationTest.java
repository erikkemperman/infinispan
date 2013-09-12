package org.infinispan.persistence.jdbc.mixed;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.factories.KnownComponentNames;
import org.infinispan.persistence.ParallelIterationTest;
import org.infinispan.persistence.jdbc.configuration.JdbcBinaryStoreConfigurationBuilder;
import org.infinispan.persistence.jdbc.configuration.JdbcMixedStoreConfigurationBuilder;
import org.infinispan.persistence.keymappers.DefaultTwoWayKey2StringMapper;
import org.infinispan.test.fwk.UnitTestDatabaseManager;
import org.testng.annotations.Test;

/**
 * @author Mircea Markus
 * @since 6.0
 */
@Test (groups = "functional", testName = "persistence.jdbc.JdbcMixedCacheStoreParallelIterationTest")
public class JdbcMixedCacheStoreParallelIterationTest extends ParallelIterationTest {

   @Override
   protected int numThreads() {
      return KnownComponentNames.getDefaultThreads(KnownComponentNames.PERSISTENCE_EXECUTOR) + 1 /** caller's thread */;
   }

   @Override
   protected void configurePersistence(ConfigurationBuilder cb) {
      JdbcMixedStoreConfigurationBuilder storeBuilder =
            cb.persistence()
               .addStore(JdbcMixedStoreConfigurationBuilder.class);
      UnitTestDatabaseManager.configureUniqueConnectionFactory(storeBuilder);
      UnitTestDatabaseManager.buildTableManipulation(storeBuilder.stringTable(), false);
      UnitTestDatabaseManager.buildTableManipulation(storeBuilder.binaryTable(), true);
      storeBuilder
            .stringTable()
            .tableNamePrefix("STRINGS_TABLE")
            .key2StringMapper(DefaultTwoWayKey2StringMapper.class)
            .binaryTable()
            .tableNamePrefix("BINARY_TABLE");

   }

}
