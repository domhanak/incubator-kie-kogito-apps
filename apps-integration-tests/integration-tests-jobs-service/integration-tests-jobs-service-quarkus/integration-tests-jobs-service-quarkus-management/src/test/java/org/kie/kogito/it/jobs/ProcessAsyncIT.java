/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.it.jobs;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.test.resources.JobServiceQuarkusTestResource;
import org.kie.kogito.test.resources.KogitoServiceRandomPortQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

@QuarkusIntegrationTest
@QuarkusTestResource(KogitoServiceRandomPortQuarkusTestResource.class)
@QuarkusTestResource(JobServiceQuarkusTestResource.class)
class ProcessAsyncIT extends BaseProcessAsyncIT {

    @BeforeEach
    private void setup() {
        //health check - wait to be ready
        await()
                .atMost(TIMEOUT)
                .pollDelay(2, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .get("/q/health")
                        .then()
                        .statusCode(200));
    }
}