package com.lmax.solana4j.client.jsonrpc;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// https://solana.com/docs/rpc/http/getslot
public class GetSlotContractTest extends SolanaClientIntegrationTestBase
{
    @Test
    void shouldGetSlot()
    {
//        {
//            "jsonrpc" : "2.0",
//                "result" : 369,
//                "id" : 4
//        }
        assertThat(api.getSlot()).isNotNull();
    }
}
