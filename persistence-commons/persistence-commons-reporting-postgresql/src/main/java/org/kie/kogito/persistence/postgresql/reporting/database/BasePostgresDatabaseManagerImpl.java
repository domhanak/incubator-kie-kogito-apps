/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.persistence.postgresql.reporting.database;

import java.util.List;

import javax.transaction.Transactional;

import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.IndexesSqlBuilderImpl;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresContext;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.TableSqlBuilderImpl;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.TriggerDeleteSqlBuilderImpl;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.TriggerInsertSqlBuilderImpl;
import org.kie.kogito.persistence.postgresql.reporting.model.JsonType;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMapping;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMappingDefinition;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresPartitionField;
import org.kie.kogito.persistence.postgresql.reporting.model.paths.PostgresTerminalPathSegment;
import org.kie.kogito.persistence.reporting.database.BaseDatabaseManagerImpl;
import org.kie.kogito.persistence.reporting.model.paths.PathSegment;
import org.kie.kogito.persistence.reporting.model.paths.TerminalPathSegment;

import static org.kie.kogito.persistence.reporting.database.Validations.validateFieldMappings;
import static org.kie.kogito.persistence.reporting.database.Validations.validateMappingId;
import static org.kie.kogito.persistence.reporting.database.Validations.validateSourceTableIdentityFields;
import static org.kie.kogito.persistence.reporting.database.Validations.validateSourceTableJsonFieldName;
import static org.kie.kogito.persistence.reporting.database.Validations.validateSourceTableName;
import static org.kie.kogito.persistence.reporting.database.Validations.validateSourceTablePartitionFields;
import static org.kie.kogito.persistence.reporting.database.Validations.validateTargetTableName;

public abstract class BasePostgresDatabaseManagerImpl extends BaseDatabaseManagerImpl<JsonType, PostgresField, PostgresPartitionField, PostgresMapping, PostgresMappingDefinition, PostgresContext> {

    protected BasePostgresDatabaseManagerImpl() {
        //CDI proxy
    }

    protected BasePostgresDatabaseManagerImpl(final IndexesSqlBuilderImpl indexesSqlBuilder,
            final TableSqlBuilderImpl tableSqlBuilder,
            final TriggerDeleteSqlBuilderImpl triggerDeleteSqlBuilder,
            final TriggerInsertSqlBuilderImpl triggerInsertSqlBuilder) {
        super(indexesSqlBuilder,
                tableSqlBuilder,
                triggerDeleteSqlBuilder,
                triggerInsertSqlBuilder);
    }

    @Override
    protected TerminalPathSegment<PostgresMapping> buildTerminalPathSegment(final String segment,
            final PathSegment parent,
            final PostgresMapping mapping) {
        return new PostgresTerminalPathSegment(segment, parent, mapping);
    }

    @Override
    public PostgresContext createContext(final PostgresMappingDefinition mappingDefinition) {
        final String mappingId = validateMappingId(mappingDefinition.getMappingId());
        final String sourceTableName = validateSourceTableName(mappingDefinition.getSourceTableName());
        final String sourceTableJsonFieldName = validateSourceTableJsonFieldName(mappingDefinition.getSourceTableJsonFieldName());
        final List<PostgresField> sourceTableIdentityFields = validateSourceTableIdentityFields(mappingDefinition.getSourceTableIdentityFields());
        final List<PostgresPartitionField> sourceTablePartitionFields = validateSourceTablePartitionFields(mappingDefinition.getSourceTablePartitionFields());
        final String targetTableName = validateTargetTableName(mappingDefinition.getTargetTableName());
        final List<PostgresMapping> mappings = validateFieldMappings(mappingDefinition.getFieldMappings());
        final List<PathSegment> pathSegments = parsePathSegments(mappings);

        return new PostgresContext(mappingId,
                sourceTableName,
                sourceTableJsonFieldName,
                sourceTableIdentityFields,
                sourceTablePartitionFields,
                targetTableName,
                mappings,
                pathSegments);
    }

    @Override
    @Transactional
    public void createArtifacts(final PostgresMappingDefinition mappingDefinition) {
        super.createArtifacts(mappingDefinition);
    }

    @Override
    @Transactional
    public void destroyArtifacts(final PostgresMappingDefinition mappingDefinition) {
        super.destroyArtifacts(mappingDefinition);
    }
}