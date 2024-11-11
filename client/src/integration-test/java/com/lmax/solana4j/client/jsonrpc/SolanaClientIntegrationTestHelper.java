package com.lmax.solana4j.client.jsonrpc;

import com.lmax.solana4j.buffer.ByteBufferPrimitiveArray;
import com.lmax.solana4j.Solana;
import com.lmax.solana4j.api.Blockhash;
import com.lmax.solana4j.api.PublicKey;
import com.lmax.solana4j.api.SignedMessageBuilder;
import com.lmax.solana4j.programs.TokenProgram;
import com.lmax.solana4j.sign.BouncyCastleSigner;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;

class SolanaClientIntegrationTestHelper
{
    String createMintToTransactionBlob(
            final PublicKey payer,
            final Blockhash recentBlockhash,
            final PublicKey tokenMint,
            final PublicKey tokenMintAuthority,
            final PublicKey destinationAddress,
            final long amount,
            final List<Signer> signers)
    {
        final ByteBuffer buffer = ByteBuffer.allocate(Solana.MAX_MESSAGE_SIZE);
        Solana.builder(buffer)
                .v0()
                .recent(recentBlockhash)
                .instructions(
                        tb -> TokenProgram.factory(tb)
                                .mintTo(
                                        tokenMint,
                                        tokenMintAuthority,
                                        List.of(Solana.destination(destinationAddress, amount))
                                )
                )
                .payer(payer)
                .seal();

        final SignedMessageBuilder signedMessageBuilder = Solana.forSigning(buffer);
        for (final Signer signer : signers)
        {
            signedMessageBuilder.by(signer.signer, (transaction, signature) -> BouncyCastleSigner.sign(signer.privateKey, transaction, signature));
        }

        return Base64.getEncoder().encodeToString(ByteBufferPrimitiveArray.copy(buffer));
    }

    static class Signer
    {
        private final PublicKey signer;
        private final byte[] privateKey;

        Signer(final PublicKey signer, final byte[] privateKey)
        {
            this.signer = signer;
            this.privateKey = privateKey;
        }
    }
}
