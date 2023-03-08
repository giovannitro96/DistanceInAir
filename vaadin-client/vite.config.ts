import { UserConfigFn } from 'vite';
import { overrideVaadinConfig } from './vite.generated';
import viteConfig from "./vite.config";

const customConfig: UserConfigFn = (env) => ({
    server : {
        fs : {
            allow: ["/Users/giovannitroia/Workspace/SOASec Project/vaadin-client"]
        }
    }
});


export default overrideVaadinConfig(customConfig);
