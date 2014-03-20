/**
 * Copyright (C) FuseSource, Inc.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.commands;

import static io.fabric8.utils.FabricValidations.validateContainersName;
import io.fabric8.api.Container;
import io.fabric8.api.FabricService;
import io.fabric8.boot.commands.support.FabricCommand;

import java.util.Collection;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;

@Command(name = ContainerDelete.FUNCTION_VALUE, scope = ContainerDelete.SCOPE_VALUE, description = ContainerDelete.DESCRIPTION, detailedDescription = "classpath:containerDelete.txt")
public class ContainerDeleteAction extends AbstractContainerLifecycleAction {

    @Option(name = "-r", aliases = {"--recursive"}, multiValued = false, required = false, description = "Recursively stops and deletes all child containers")
    protected boolean recursive = false;

    ContainerDeleteAction(FabricService fabricService) {
        super(fabricService);
    }

    @Override
    protected Object doExecute() throws Exception {
        Collection<String> expandedNames = super.expandGlobNames(containers);
        for (String containerName : expandedNames) {
            validateContainersName(containerName);
            if (FabricCommand.isPartOfEnsemble(fabricService, containerName) && !force) {
                System.out
                    .println("Container is part of the ensemble. If you still want to delete it, please use -f option.");
                return null;
            }

            Container found = FabricCommand.getContainer(fabricService, containerName);
            applyUpdatedCredentials(found);
            if (recursive || force) {
                for (Container child : found.getChildren()) {
                    child.destroy(force);
                }
            }
            found.destroy(force);
        }
        return null;
    }

}
