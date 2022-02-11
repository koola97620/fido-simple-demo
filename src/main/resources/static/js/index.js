
$(window).on('load', function () {
    $("#register").on('click', () => registerButtonClicked());
    $("#authenticate").on('click', () => authenticateButtonClicked());


});

const abortController = new AbortController();
const abortSignal = abortController.signal;


function registerButtonClicked() {
    let loginId  = $("input[name='loginId']").val();
    if (loginId === "") {
        $("#status").text("Input user name first");
        $("#status").removeClass('hidden');
    }

    disableControls();
    $("#registerSpinner").removeClass("hidden");

    let serverPublicKeyCredentialCreationOptionsRequest = {
        loginId: loginId
    };

    getRegChallenge(serverPublicKeyCredentialCreationOptionsRequest)
        .then(createCredentialOptions => {
            return createCredential(createCredentialOptions);
        })
        .then(() => {
            $("#status").text("Successfully created credential");
            $("#registerSpinner").addClass("hidden");
            enableControls();
        })
        .catch(e => {
            $("#status").text("Error: " + e);
            $("#registerSpinner").addClass("hidden");
            enableControls();
        });

}

function authenticateButtonClicked() {
    console.log("=== authenticateButtonClicked")
}


function disableControls() {
    $('#register').attr('disabled','');
    $('#authenticate').attr('disabled','');
    $("#status").addClass('hidden');
}

function enableControls() {
    $('#register').removeAttr('disabled');
    $('#authenticate').removeAttr('disabled');
    $("#status").removeClass('hidden');
}

function getRegChallenge(serverPublicKeyCredentialCreationOptionsRequest) {
    logObject("Get reg challenge", serverPublicKeyCredentialCreationOptionsRequest);
    return rest_post("/rp/attestation/options", serverPublicKeyCredentialCreationOptionsRequest)
        .then(response => {
            logObject("Get reg challenge response", response);
            if (response.status !== 'ok') {
                return Promise.reject(response.errorMessage);
            } else {
                let createCredentialOptions = performMakeCredReq(response);
                return Promise.resolve(createCredentialOptions);
            }
        });
}

function createCredential(options) {
    if (!PublicKeyCredential || typeof PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable !== "function") {
        return Promise.reject("WebAuthn APIs are not available on this user agent.");
    }

    return navigator.credentials.create({publicKey: options, signal: abortSignal})
        .then(rawAttestation => {
            logObject("raw attestation", rawAttestation);
            let attestation = {
                rawId: base64UrlEncode(rawAttestation.rawId),
                id: base64UrlEncode(rawAttestation.rawId),
                response : {
                    clientDataJSON: base64UrlEncode(rawAttestation.response.clientDataJSON),
                    attestationObject: base64UrlEncode(rawAttestation.response.attestationObject)
                },
                type: rawAttestation.type,
            };

            if (rawAttestation.getClientExtensionResults) {
                attestation.extensions = rawAttestation.getClientExtensionResults();
            }

            // set transports if it is available
            if (typeof rawAttestation.response.getTransports === "function") {
                attestation.response.transports = rawAttestation.response.getTransports();
            }

            console.log("=== Attestation response ===");
            logVariable("rawId (b64url)", attestation.rawId)
            logVariable("id (b64url)", attestation.id);
            logVariable("response.clientDataJSON (b64url)", attestation.response.clientDataJSON);
            logVariable("response.attestationObject (b64url)", attestation.response.attestationObject);
            logVariable("response.transports", attestation.response.transports);
            logVariable("id", attestation.type);

            return rest_post("/rp/attestation/result", attestation);
        })
        .catch(function(error) {
            logVariable("create credential error", error);
            if (error == "AbortError") {
                console.info("Aborted by user");
            }
            return Promise.reject(error);
        })
        .then(response => {
            if (response.status !== 'ok') {
                return Promise.reject(response.errorMessage);
            } else {
                return Promise.resolve(response);
            }
        });
}

function logObject(name, object) {
    console.log(name + ": " + JSON.stringify(object));
}

function logVariable(name, text) {
    console.log(name + ": " + text);
}

function base64UrlEncode(arrayBuffer) {
    if (!arrayBuffer || arrayBuffer.length == 0) {
        return undefined;
    }

    return btoa(String.fromCharCode.apply(null, new Uint8Array(arrayBuffer)))
        .replace(/=/g, "")
        .replace(/\+/g, "-")
        .replace(/\//g, "_");
}



function rest_post(endpoint, object) {
    return fetch(endpoint, {
        method: "POST",
        credentials: "same-origin",
        body: JSON.stringify(object),
        headers: {
            "content-type": "application/json"
        }
    })
        .then(response => {
            return response.json();
        });
}