/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openhubframework.openhub.admin.web.common.rpc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

import org.openhubframework.openhub.api.entity.Identifiable;
import org.openhubframework.openhub.api.exception.ValidationIntegrationException;


/**
 * Reference-able RPC entity.
 * </p>
 * There is the following lifecycle when entity is <strong>created</strong> from RPC:
 * <ol>
 *     <li>validates attributes and state of the RPC - {@link #validate()}
 *     <li>creates new entity instance - {@link #createEntityInstance(Map)}
 *     <li>creates entity - {@link #createEntity()}
 *     <li>updates attributes - {@link #updateAttributes(Identifiable, boolean)}
 * </ol>
 *
 * </p>
 * There is the following lifecycle when entity is <strong>updated</strong> from RPC:
 * <ol>
 *     <li>validates attributes and state of the RPC - {@link #validate()}
 *     <li>updates entity - {@link #updateEntity(Identifiable)}
 *     <li>updates attributes - {@link #updateAttributes(Identifiable, boolean)}
 * </ol>
 *
 * @author <a href="mailto:petr.juza@openwise.cz">Petr Juza</a>
 * @since 2.0
 */
public abstract class BaseRpc<T extends Identifiable<ID>, ID extends Serializable> implements Identifiable<ID> {

    private ID id;

    /**
     * Empty constructor for deserialization from XML/JSON.
     */
    public BaseRpc() {
        init();
    }

    public BaseRpc(T entity) {
        Assert.notNull(entity, "entity can not be null");

        init();

        this.id = entity.getId();
    }

    @Nullable
    @Override
    @XmlAttribute
    public ID getId() {
        return id;
    }

    @Override
    public void setId(@Nullable final ID id) {
        this.id = id;
    }

    /**
     * Inits RPC => override it for specified needs.
     */
    protected void init() {
        // nothing to implement by default
    }

    /**
     * Validates RPC (mandatory attributes, correct object state etc.)
     *
     * @throws ValidationIntegrationException when there is error in input data
     */
    protected abstract void validate() throws ValidationIntegrationException;

    /**
     * Creates new entity instance from RPC.
     *
     * @param params Input parameters from client for creating new instance
     * @return new entity instance
     */
    protected abstract T createEntityInstance(Map<String, ?> params);

    /**
     * Creates new entity from RPC.
     *
     * @return new entity
     */
    public final T createEntity() {
        return createEntity(new HashMap<>());
    }

    /**
     * Creates new entity from RPC.
     *
     * @param params Input parameters from client for creating new instance
     * @return new entity
     */
    public final T createEntity(Map<String, ?> params) {
        Assert.notNull(params, "params can not be null");

        validate();

        T entity = createEntityInstance(params);

        Assert.notNull(entity, "entity can not be null");

        updateAttributes(entity, true);

        return entity;
    }

    /**
     * Updates existing entity from RPC.
     *
     * @param entity The existing entity
     */
    public final void updateEntity(T entity) {
        Assert.notNull(entity, "entity can not be null");

        validate();

        updateAttributes(entity, false);
    }

    /**
     * Updates attributes of specified entity from RPC values.
     *
     * @param entity The entity
     * @param created {@code true} if new entity is created or {@code false} if entity is updated
     */
    protected void updateAttributes(T entity, boolean created) {
        // nothing to implement by default
    }

    /**
     * Two entities are equal if their key values are equal.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof BaseRpc) {
            BaseRpc en = (BaseRpc) obj;

            return new EqualsBuilder()
                    .append(id, en.id)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .toString();
    }
}
