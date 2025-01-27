declare module 'wavesurfer.js' {
  export default class WaveSurfer {
    setVolume(arg0: number): () => void;
    static create(options: any): WaveSurfer;
    on(event: string, callback: () => void): void;
    load(url: string): void;
    playPause(): void;
    stop(): void;
  }
}

declare module 'wavesurfer.js/dist/plugins/spectrogram.js' {
  export default {
    create: (options: any) => any,
  };
}

declare module 'wavesurfer.js/dist/plugins/timeline.js' {
  export default {
    create: (options: any) => any,
  };
}

declare module 'wavesurfer.js/dist/plugins/regions.js' {
  export default {
    create: (options: any) => any,
  };
}
