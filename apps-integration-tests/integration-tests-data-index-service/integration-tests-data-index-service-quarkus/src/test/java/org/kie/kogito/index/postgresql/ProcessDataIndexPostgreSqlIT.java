/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.index.postgresql;

import org.kie.kogito.index.AbstractProcessDataIndexIT;
import org.kie.kogito.index.quarkus.DataIndexPostgreSqlQuarkusTestResource;
import org.kie.kogito.index.quarkus.PostgreSqlTestProfile;
import org.kie.kogito.test.quarkus.QuarkusTestProperty;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@TestProfile(PostgreSqlTestProfile.class)
public class ProcessDataIndexPostgreSqlIT extends AbstractProcessDataIndexIT {

    @QuarkusTestProperty(name = DataIndexPostgreSqlQuarkusTestResource.KOGITO_DATA_INDEX_SERVICE_URL)
    String dataIndex;

    @Override
    public String getDataIndexURL() {
        return dataIndex;
    }

    @Override
    public boolean validateDomainData() {
        return false;
    }

    @Override
    public boolean validateGetProcessInstanceSource() {
        return true;
    }
}