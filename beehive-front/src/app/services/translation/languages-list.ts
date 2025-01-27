export const languages: Language[] = [
  { name: 'English', icon: 'global', key: 'en' },
  { name: 'Spanish', icon: 'global', key: 'es' },
  // { name: 'French', icon: 'global', key: 'fr' },
];

export interface Language {
  name: string;
  icon: string;
  key: string;
}
