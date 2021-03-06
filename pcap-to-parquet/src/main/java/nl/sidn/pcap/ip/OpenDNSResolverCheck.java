/*
 * ENTRADA, a big data platform for network data analytics
 *
 * Copyright (C) 2016 SIDN [https://www.sidn.nl]
 * 
 * This file is part of ENTRADA.
 * 
 * ENTRADA is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * ENTRADA is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with ENTRADA. If not, see
 * [<http://www.gnu.org/licenses/].
 *
 */
package nl.sidn.pcap.ip;

import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.sidn.pcap.util.Settings;

/**
 * Checker to see if an IP address is an OpenDNS resolver. resolver ips are downloaded from opendns
 * website, url: https://www.opendns.com/network-map-data
 * 
 * @author maarten
 *
 */
public final class OpenDNSResolverCheck extends AbstractNetworkCheck {

  private static String OPENDNS_RESOLVER_IP_FILENAME = "opendns-resolvers";
  protected static final Logger LOGGER = LoggerFactory.getLogger(OpenDNSResolverCheck.class);

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void init() {
    String url = Settings.getInstance().getSetting(Settings.RESOLVER_LIST_OPENDNS);
    LOGGER.info("Load OpenDNS resolver addresses from url: " + url);

    int timeout = 15;
    RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
        .setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
    CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    try {
      HttpGet get = new HttpGet(url);
      CloseableHttpResponse response = client.execute(get);

      List<Map<String, String>> locations =
          objectMapper.readValue(response.getEntity().getContent(), List.class);

      for (Map<String, String> location : locations) {
        String v4 = location.get("subnetV4");
        String v6 = location.get("subnetV6");

        LOGGER.info("Add OpenDNS resolver IP ranges, subnetV4: " + v4 + " subnetV6: " + v6);

        bit_subnets.add(Subnet.createInstance(v4));
        bit_subnets.add(Subnet.createInstance(v6));
        subnets.add(v4);
        subnets.add(v6);
      }

    } catch (Exception e) {
      LOGGER.error("Problem while adding OpenDns resolvers.");
    } finally {
      IOUtils.closeQuietly(client);
    }

  }

  @Override
  protected String getFilename() {
    return OPENDNS_RESOLVER_IP_FILENAME;
  }


}
