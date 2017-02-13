/*
 * Copyright 2015-2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
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
package org.hawkular.wildfly.agent.installer;

import java.io.File;

import org.hawkular.cmdgw.ws.test.TestWebSocketClient;
import org.hawkular.cmdgw.ws.test.TestWebSocketClient.MessageAnswer;
import org.hawkular.dmrclient.Address;
import org.hawkular.inventory.paths.CanonicalPath;
import org.hawkular.wildfly.agent.itest.util.AbstractITest;
import org.hawkular.wildfly.agent.itest.util.WildFlyClientConfig;
import org.jboss.as.controller.client.ModelControllerClient;
import org.testng.annotations.Test;

public class DomainDeployApplicationITest extends AbstractITest {
    public static final String GROUP = "DomainDeployApplicationITest";

    @Test(groups = { GROUP }, dependsOnGroups = { AgentInstallerDomainITest.GROUP })
    public void testAddDeployment() throws Throwable {
        waitForAccountsAndInventory();

        WildFlyClientConfig clientConfig = getPlainWildFlyClientConfig();
        CanonicalPath wfPath = getHostController(clientConfig);
        File applicationFile = getTestApplicationFile();
        final String deploymentName = applicationFile.getName();

        String req = "DeployApplicationRequest={\"authentication\":" + authentication + ", "
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"serverGroups\":\"main-server-group,other-server-group\""
                + "}";
        String response = "DeployApplicationResponse={"
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationSessionId\":\"{{sessionId}}\","
                + "\"status\":\"OK\","
                + "\"message\":\"Performed [Deploy] on a [Application] given by Inventory path ["
                + wfPath.toString() + "]\""
                + "}";
        try (TestWebSocketClient testClient = TestWebSocketClient.builder()
                .url(baseGwUri + "/ui/ws")
                .expectWelcome(new MessageAnswer(req, applicationFile.toURI().toURL(), 0))
                .expectGenericSuccess(wfPath.ids().getFeedId())
                .expectText(response, TestWebSocketClient.Answer.CLOSE)
                .expectClose()
                .build()) {
            testClient.validate(30000);
        }

        // check that the app was really deployed on the two server groups
        try (ModelControllerClient mcc = newPlainWildFlyModelControllerClient(clientConfig)) {
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=main-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=other-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
        }
    }

    @Test(groups = { GROUP }, dependsOnMethods = { "testAddDeployment" })
    public void testDisableDeployment() throws Throwable {
        waitForAccountsAndInventory();
        WildFlyClientConfig clientConfig = getPlainWildFlyClientConfig();

        // check that the app is enabled on the two server groups
        try (ModelControllerClient mcc = newPlainWildFlyModelControllerClient(clientConfig)) {
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=main-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=other-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
        }

        CanonicalPath wfPath = getHostController(clientConfig);
        File applicationFile = getTestApplicationFile();
        final String deploymentName = applicationFile.getName();

        String req = "DisableApplicationRequest={\"authentication\":" + authentication + ", "
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"serverGroups\":\"main-server-group,other-server-group\""
                + "}";
        String response = "DisableApplicationResponse={"
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationSessionId\":\"{{sessionId}}\","
                + "\"status\":\"OK\","
                + "\"message\":\"Performed [Disable Deployment] on a [Application] given by Inventory path ["
                + wfPath.toString() + "]\""
                + "}";
        try (TestWebSocketClient testClient = TestWebSocketClient.builder()
                .url(baseGwUri + "/ui/ws")
                .expectWelcome(new MessageAnswer(req, applicationFile.toURI().toURL(), 0))
                .expectGenericSuccess(wfPath.ids().getFeedId())
                .expectText(response, TestWebSocketClient.Answer.CLOSE)
                .expectClose()
                .build()) {
            testClient.validate(30000);
        }

        // check that the app is disabled on the two server groups
        try (ModelControllerClient mcc = newPlainWildFlyModelControllerClient(clientConfig)) {
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=main-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "false");
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=other-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "false");
        }
    }

    @Test(groups = { GROUP }, dependsOnMethods = { "testDisableDeployment" })
    public void testEnableDeployment() throws Throwable {
        waitForAccountsAndInventory();
        WildFlyClientConfig clientConfig = getPlainWildFlyClientConfig();

        // check that the app is disabled on the two server groups
        try (ModelControllerClient mcc = newPlainWildFlyModelControllerClient(clientConfig)) {
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=main-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "false");
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=other-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "false");
        }

        CanonicalPath wfPath = getHostController(clientConfig);
        File applicationFile = getTestApplicationFile();
        final String deploymentName = applicationFile.getName();

        String req = "EnableApplicationRequest={\"authentication\":" + authentication + ", "
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"serverGroups\":\"main-server-group,other-server-group\""
                + "}";
        String response = "EnableApplicationResponse={"
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationSessionId\":\"{{sessionId}}\","
                + "\"status\":\"OK\","
                + "\"message\":\"Performed [Enable Deployment] on a [Application] given by Inventory path ["
                + wfPath.toString() + "]\""
                + "}";
        try (TestWebSocketClient testClient = TestWebSocketClient.builder()
                .url(baseGwUri + "/ui/ws")
                .expectWelcome(new MessageAnswer(req, applicationFile.toURI().toURL(), 0))
                .expectGenericSuccess(wfPath.ids().getFeedId())
                .expectText(response, TestWebSocketClient.Answer.CLOSE)
                .expectClose()
                .build()) {
            testClient.validate(30000);
        }

        // check that the app is enabled on the two server groups
        try (ModelControllerClient mcc = newPlainWildFlyModelControllerClient(clientConfig)) {
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=main-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=other-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
        }
    }

    @Test(groups = { GROUP }, dependsOnMethods = { "testEnableDeployment" })
    public void testRestartDeployment() throws Throwable {
        waitForAccountsAndInventory();
        WildFlyClientConfig clientConfig = getPlainWildFlyClientConfig();

        // check that the app is enabled on the two server groups
        try (ModelControllerClient mcc = newPlainWildFlyModelControllerClient(clientConfig)) {
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=main-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=other-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
        }

        CanonicalPath wfPath = getHostController(clientConfig);
        File applicationFile = getTestApplicationFile();
        final String deploymentName = applicationFile.getName();

        String req = "RestartApplicationRequest={\"authentication\":" + authentication + ", "
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"serverGroups\":\"main-server-group,other-server-group\""
                + "}";
        String response = "RestartApplicationResponse={"
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationSessionId\":\"{{sessionId}}\","
                + "\"status\":\"OK\","
                + "\"message\":\"Performed [Restart Deployment] on a [Application] given by Inventory path ["
                + wfPath.toString() + "]\""
                + "}";
        try (TestWebSocketClient testClient = TestWebSocketClient.builder()
                .url(baseGwUri + "/ui/ws")
                .expectWelcome(new MessageAnswer(req, applicationFile.toURI().toURL(), 0))
                .expectGenericSuccess(wfPath.ids().getFeedId())
                .expectText(response, TestWebSocketClient.Answer.CLOSE)
                .expectClose()
                .build()) {
            testClient.validate(30000);
        }

        // check that the app is again enabled on the two server groups
        try (ModelControllerClient mcc = newPlainWildFlyModelControllerClient(clientConfig)) {
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=main-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
            assertNodeAttributeEquals(mcc,
                    Address.parse(
                            "/server-group=other-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war")
                            .getAddressNode(),
                    "enabled",
                    "true");
        }
    }

    // this does a full undeploy via the host controller - removing from all server groups
    @Test(groups = { GROUP }, dependsOnMethods = { "testRestartDeployment" })
    public void testUndeploy() throws Throwable {
        waitForAccountsAndInventory();

        WildFlyClientConfig clientConfig = getPlainWildFlyClientConfig();
        CanonicalPath wfPath = getHostController(clientConfig);
        File applicationFile = getTestApplicationFile();
        final String deploymentName = applicationFile.getName();

        String req = "UndeployApplicationRequest={\"authentication\":" + authentication + ", "
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"serverGroups\":\"main-server-group,other-server-group\""
                + "}";
        String response = "UndeployApplicationResponse={"
                + "\"destinationFileName\":\"" + deploymentName + "\","
                + "\"resourcePath\":\"" + wfPath.toString() + "\","
                + "\"destinationSessionId\":\"{{sessionId}}\","
                + "\"status\":\"OK\","
                + "\"message\":\"Performed [Undeploy] on a [Application] given by Inventory path ["
                + wfPath.toString() + "]\""
                + "}";
        try (TestWebSocketClient testClient = TestWebSocketClient.builder()
                .url(baseGwUri + "/ui/ws")
                .expectWelcome(new MessageAnswer(req, applicationFile.toURI().toURL(), 0))
                .expectGenericSuccess(wfPath.ids().getFeedId())
                .expectText(response, TestWebSocketClient.Answer.CLOSE)
                .expectClose()
                .build()) {
            testClient.validate(30000);
        }

        // check that the app was really undeployed on the two server groups
        try (ModelControllerClient mcc = newPlainWildFlyModelControllerClient(clientConfig)) {
            assertResourceExists(mcc,
                    Address.parse(
                            "/server-group=main-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war/")
                            .getAddressNode(),
                    false);
            assertResourceExists(mcc,
                    Address.parse(
                            "/server-group=other-server-group/deployment=hawkular-wildfly-agent-helloworld-war.war/")
                            .getAddressNode(),
                    false);
        }
    }
}
